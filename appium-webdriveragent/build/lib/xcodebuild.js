"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.XcodeBuild = void 0;
const asyncbox_1 = require("asyncbox");
const teen_process_1 = require("teen_process");
const support_1 = require("@appium/support");
const logger_1 = __importDefault(require("./logger"));
const bluebird_1 = __importDefault(require("bluebird"));
const utils_1 = require("./utils");
const lodash_1 = __importDefault(require("lodash"));
const path_1 = __importDefault(require("path"));
const os_1 = require("os");
const constants_1 = require("./constants");
const node_readline_1 = __importDefault(require("node:readline"));
const DEFAULT_SIGNING_ID = 'iPhone Developer';
const PREBUILD_DELAY = 0;
const RUNNER_SCHEME_IOS = 'WebDriverAgentRunner';
const LIB_SCHEME_IOS = 'WebDriverAgentLib';
const ERROR_WRITING_ATTACHMENT = 'Error writing attachment data to file';
const ERROR_COPYING_ATTACHMENT = 'Error copying testing attachment';
const IGNORED_ERRORS = [
    ERROR_WRITING_ATTACHMENT,
    ERROR_COPYING_ATTACHMENT,
    'Failed to remove screenshot at path',
];
const RUNNER_SCHEME_TV = 'WebDriverAgentRunner_tvOS';
const LIB_SCHEME_TV = 'WebDriverAgentLib_tvOS';
const xcodeLog = support_1.logger.getLogger('Xcode');
class XcodeBuild {
    /**
     * @param {string} xcodeVersion
     * @param {any} device
     * @param {any} args
     * @param {import('@appium/types').AppiumLogger?} log
     */
    constructor(xcodeVersion, device, args = {}, log = null) {
        this.xcodeVersion = xcodeVersion;
        this.device = device;
        this.log = log ?? logger_1.default;
        this.realDevice = args.realDevice;
        this.agentPath = args.agentPath;
        this.bootstrapPath = args.bootstrapPath;
        this.platformVersion = args.platformVersion;
        this.platformName = args.platformName;
        this.iosSdkVersion = args.iosSdkVersion;
        this.showXcodeLog = args.showXcodeLog;
        this.xcodeConfigFile = args.xcodeConfigFile;
        this.xcodeOrgId = args.xcodeOrgId;
        this.xcodeSigningId = args.xcodeSigningId || DEFAULT_SIGNING_ID;
        this.keychainPath = args.keychainPath;
        this.keychainPassword = args.keychainPassword;
        this.prebuildWDA = args.prebuildWDA;
        this.usePrebuiltWDA = args.usePrebuiltWDA;
        this.useSimpleBuildTest = args.useSimpleBuildTest;
        this.useXctestrunFile = args.useXctestrunFile;
        this.launchTimeout = args.launchTimeout;
        this.wdaRemotePort = args.wdaRemotePort;
        this.updatedWDABundleId = args.updatedWDABundleId;
        this.derivedDataPath = args.derivedDataPath;
        this.mjpegServerPort = args.mjpegServerPort;
        this.prebuildDelay = lodash_1.default.isNumber(args.prebuildDelay) ? args.prebuildDelay : PREBUILD_DELAY;
        this.allowProvisioningDeviceRegistration = args.allowProvisioningDeviceRegistration;
        this.resultBundlePath = args.resultBundlePath;
        this.resultBundleVersion = args.resultBundleVersion;
    }
    async init(noSessionProxy) {
        this.noSessionProxy = noSessionProxy;
        if (this.useXctestrunFile) {
            const deviveInfo = {
                isRealDevice: this.realDevice,
                udid: this.device.udid,
                platformVersion: this.platformVersion,
                platformName: this.platformName
            };
            this.xctestrunFilePath = await (0, utils_1.setXctestrunFile)(deviveInfo, this.iosSdkVersion, this.bootstrapPath, this.wdaRemotePort);
            return;
        }
        // if necessary, update the bundleId to user's specification
        if (this.realDevice) {
            // In case the project still has the user specific bundle ID, reset the project file first.
            // - We do this reset even if updatedWDABundleId is not specified,
            //   since the previous updatedWDABundleId test has generated the user specific bundle ID project file.
            // - We don't call resetProjectFile for simulator,
            //   since simulator test run will work with any user specific bundle ID.
            await (0, utils_1.resetProjectFile)(this.agentPath);
            if (this.updatedWDABundleId) {
                await (0, utils_1.updateProjectFile)(this.agentPath, this.updatedWDABundleId);
            }
        }
    }
    async retrieveDerivedDataPath() {
        if (this.derivedDataPath) {
            return this.derivedDataPath;
        }
        // avoid race conditions
        if (this._derivedDataPathPromise) {
            return await this._derivedDataPathPromise;
        }
        this._derivedDataPathPromise = (async () => {
            let stdout;
            try {
                ({ stdout } = await (0, teen_process_1.exec)('xcodebuild', ['-project', this.agentPath, '-showBuildSettings']));
            }
            catch (err) {
                this.log.warn(`Cannot retrieve WDA build settings. Original error: ${err.message}`);
                return;
            }
            const pattern = /^\s*BUILD_DIR\s+=\s+(\/.*)/m;
            const match = pattern.exec(stdout);
            if (!match) {
                this.log.warn(`Cannot parse WDA build dir from ${lodash_1.default.truncate(stdout, { length: 300 })}`);
                return;
            }
            this.log.debug(`Parsed BUILD_DIR configuration value: '${match[1]}'`);
            // Derived data root is two levels higher over the build dir
            this.derivedDataPath = path_1.default.dirname(path_1.default.dirname(path_1.default.normalize(match[1])));
            this.log.debug(`Got derived data root: '${this.derivedDataPath}'`);
            return this.derivedDataPath;
        })();
        return await this._derivedDataPathPromise;
    }
    async reset() {
        // if necessary, reset the bundleId to original value
        if (this.realDevice && this.updatedWDABundleId) {
            await (0, utils_1.resetProjectFile)(this.agentPath);
        }
    }
    async prebuild() {
        // first do a build phase
        this.log.debug('Pre-building WDA before launching test');
        this.usePrebuiltWDA = true;
        await this.start(true);
        this.xcodebuild = null;
        // pause a moment
        await bluebird_1.default.delay(this.prebuildDelay);
    }
    async cleanProject() {
        const libScheme = (0, utils_1.isTvOS)(this.platformName) ? LIB_SCHEME_TV : LIB_SCHEME_IOS;
        const runnerScheme = (0, utils_1.isTvOS)(this.platformName) ? RUNNER_SCHEME_TV : RUNNER_SCHEME_IOS;
        for (const scheme of [libScheme, runnerScheme]) {
            this.log.debug(`Cleaning the project scheme '${scheme}' to make sure there are no leftovers from previous installs`);
            await (0, teen_process_1.exec)('xcodebuild', [
                'clean',
                '-project', this.agentPath,
                '-scheme', scheme,
            ]);
        }
    }
    getCommand(buildOnly = false) {
        let cmd = 'xcodebuild';
        let args;
        // figure out the targets for xcodebuild
        const [buildCmd, testCmd] = this.useSimpleBuildTest ? ['build', 'test'] : ['build-for-testing', 'test-without-building'];
        if (buildOnly) {
            args = [buildCmd];
        }
        else if (this.usePrebuiltWDA || this.useXctestrunFile) {
            args = [testCmd];
        }
        else {
            args = [buildCmd, testCmd];
        }
        if (this.allowProvisioningDeviceRegistration) {
            // To -allowProvisioningDeviceRegistration flag takes effect, -allowProvisioningUpdates needs to be passed as well.
            args.push('-allowProvisioningUpdates', '-allowProvisioningDeviceRegistration');
        }
        if (this.resultBundlePath) {
            args.push('-resultBundlePath', this.resultBundlePath);
        }
        if (this.resultBundleVersion) {
            args.push('-resultBundleVersion', this.resultBundleVersion);
        }
        if (this.useXctestrunFile && this.xctestrunFilePath) {
            args.push('-xctestrun', this.xctestrunFilePath);
        }
        else {
            const runnerScheme = (0, utils_1.isTvOS)(this.platformName) ? RUNNER_SCHEME_TV : RUNNER_SCHEME_IOS;
            args.push('-project', this.agentPath, '-scheme', runnerScheme);
            if (this.derivedDataPath) {
                args.push('-derivedDataPath', this.derivedDataPath);
            }
        }
        args.push('-destination', `id=${this.device.udid}`);
        const versionMatch = new RegExp(/^(\d+)\.(\d+)/).exec(this.platformVersion);
        if (versionMatch) {
            args.push(`${(0, utils_1.isTvOS)(this.platformName) ? 'TV' : 'IPHONE'}OS_DEPLOYMENT_TARGET=${versionMatch[1]}.${versionMatch[2]}`);
        }
        else {
            this.log.warn(`Cannot parse major and minor version numbers from platformVersion "${this.platformVersion}". ` +
                'Will build for the default platform instead');
        }
        if (this.realDevice && this.xcodeConfigFile) {
            this.log.debug(`Using Xcode configuration file: '${this.xcodeConfigFile}'`);
            args.push('-xcconfig', this.xcodeConfigFile);
        }
        if (!process.env.APPIUM_XCUITEST_TREAT_WARNINGS_AS_ERRORS) {
            // This sometimes helps to survive Xcode updates
            args.push('GCC_TREAT_WARNINGS_AS_ERRORS=0');
        }
        // Below option slightly reduces build time in debug build
        // with preventing to generate `/Index/DataStore` which is used by development
        args.push('COMPILER_INDEX_STORE_ENABLE=NO');
        return { cmd, args };
    }
    async createSubProcess(buildOnly = false) {
        if (!this.useXctestrunFile && this.realDevice) {
            if (this.keychainPath && this.keychainPassword) {
                await (0, utils_1.setRealDeviceSecurity)(this.keychainPath, this.keychainPassword);
            }
            if (this.xcodeOrgId && this.xcodeSigningId && !this.xcodeConfigFile) {
                this.xcodeConfigFile = await (0, utils_1.generateXcodeConfigFile)(this.xcodeOrgId, this.xcodeSigningId);
            }
        }
        const { cmd, args } = this.getCommand(buildOnly);
        this.log.debug(`Beginning ${buildOnly ? 'build' : 'test'} with command '${cmd} ${args.join(' ')}' ` +
            `in directory '${this.bootstrapPath}'`);
        /** @type {Record<string, any>} */
        const env = Object.assign({}, process.env, {
            USE_PORT: this.wdaRemotePort,
            WDA_PRODUCT_BUNDLE_IDENTIFIER: this.updatedWDABundleId || constants_1.WDA_RUNNER_BUNDLE_ID,
        });
        if (this.mjpegServerPort) {
            // https://github.com/appium/WebDriverAgent/pull/105
            env.MJPEG_SERVER_PORT = this.mjpegServerPort;
        }
        const upgradeTimestamp = await (0, utils_1.getWDAUpgradeTimestamp)();
        if (upgradeTimestamp) {
            env.UPGRADE_TIMESTAMP = upgradeTimestamp;
        }
        const xcodebuild = new teen_process_1.SubProcess(cmd, args, {
            cwd: this.bootstrapPath,
            env,
            detached: true,
            stdio: ['ignore', 'pipe', 'pipe'],
        });
        let logXcodeOutput = !!this.showXcodeLog;
        const logMsg = lodash_1.default.isBoolean(this.showXcodeLog)
            ? `Output from xcodebuild ${this.showXcodeLog ? 'will' : 'will not'} be logged`
            : 'Output from xcodebuild will only be logged if any errors are present there';
        this.log.debug(`${logMsg}. To change this, use 'showXcodeLog' desired capability`);
        xcodebuild.on('output', (stdout, stderr) => {
            let out = stdout || stderr;
            // we want to pull out the log file that is created, and highlight it
            // for diagnostic purposes
            if (out.includes('Writing diagnostic log for test session to')) {
                // pull out the first line that begins with the path separator
                // which *should* be the line indicating the log file generated
                // @ts-ignore logLocation is a custom property
                xcodebuild.logLocation = lodash_1.default.first(lodash_1.default.remove(out.trim().split('\n'), (v) => v.startsWith(path_1.default.sep)));
                // @ts-ignore logLocation is a custom property
                xcodeLog.debug(`Log file for xcodebuild test: ${xcodebuild.logLocation}`);
            }
            // if we have an error we want to output the logs
            // otherwise the failure is inscrutible
            // but do not log permission errors from trying to write to attachments folder
            const ignoreError = IGNORED_ERRORS.some((x) => out.includes(x));
            if (this.showXcodeLog !== false && out.includes('Error Domain=') && !ignoreError) {
                logXcodeOutput = true;
                // terrible hack to handle case where xcode return 0 but is failing
                // @ts-ignore _wda_error_occurred is a custom property
                xcodebuild._wda_error_occurred = true;
            }
            // do not log permission errors from trying to write to attachments folder
            if (logXcodeOutput && !ignoreError) {
                for (const line of out.split(os_1.EOL)) {
                    xcodeLog.error(line);
                    if (line) {
                        // @ts-ignore _wda_error_message is a custom property
                        xcodebuild._wda_error_message += `${os_1.EOL}${line}`;
                    }
                }
            }
        });
        return xcodebuild;
    }
    async start(buildOnly = false) {
        this.xcodebuild = await this.createSubProcess(buildOnly);
        // Store xcodebuild message
        // @ts-ignore _wda_error_message is a custom property
        this.xcodebuild._wda_error_message = '';
        // wrap the start procedure in a promise so that we can catch, and report,
        // any startup errors that are thrown as events
        return await new bluebird_1.default((resolve, reject) => {
            // @ts-ignore xcodebuild must be present here
            this.xcodebuild.on('exit', async (code, signal) => {
                xcodeLog.error(`xcodebuild exited with code '${code}' and signal '${signal}'`);
                // print out the xcodebuild file if users have asked for it
                // @ts-ignore logLocation is a custom property
                if (this.showXcodeLog && this.xcodebuild?.logLocation) {
                    // @ts-ignore logLocation is a custom property
                    xcodeLog.error(`Contents of xcodebuild log file '${this.xcodebuild.logLocation}':`);
                    try {
                        const logFile = node_readline_1.default.createInterface({
                            // @ts-ignore logLocation is a custom property
                            input: support_1.fs.createReadStream(this.xcodebuild.logLocation),
                            terminal: false
                        });
                        logFile.on('line', (line) => {
                            xcodeLog.error(line);
                        });
                        await new bluebird_1.default((_resolve) => {
                            logFile.once('close', () => {
                                logFile.removeAllListeners();
                                _resolve();
                            });
                        });
                    }
                    catch (err) {
                        xcodeLog.error(`Unable to access xcodebuild log file: '${err.message}'`);
                    }
                }
                // @ts-ignore processExited is a custom property
                this.xcodebuild.processExited = true;
                // @ts-ignore _wda_error_occurred is a custom property
                if (this.xcodebuild._wda_error_occurred || (!signal && code !== 0)) {
                    return reject(new Error(`xcodebuild failed with code ${code}${os_1.EOL}` +
                        // @ts-ignore _wda_error_message is a custom property
                        `xcodebuild error message:${os_1.EOL}${this.xcodebuild._wda_error_message}`));
                }
                // in the case of just building, the process will exit and that is our finish
                if (buildOnly) {
                    return resolve();
                }
            });
            return (async () => {
                try {
                    const timer = new support_1.timing.Timer().start();
                    // @ts-ignore this.xcodebuild must be defined
                    await this.xcodebuild.start(true);
                    if (!buildOnly) {
                        let status = await this.waitForStart(timer);
                        resolve(status);
                    }
                }
                catch (err) {
                    let msg = `Unable to start WebDriverAgent: ${err}`;
                    this.log.error(msg);
                    reject(new Error(msg));
                }
            })();
        });
    }
    async waitForStart(timer) {
        // try to connect once every 0.5 seconds, until `launchTimeout` is up
        this.log.debug(`Waiting up to ${this.launchTimeout}ms for WebDriverAgent to start`);
        let currentStatus = null;
        try {
            let retries = parseInt(`${this.launchTimeout / 500}`, 10);
            await (0, asyncbox_1.retryInterval)(retries, 1000, async () => {
                // @ts-ignore processExited is a custom property
                if (this.xcodebuild.processExited) {
                    // there has been an error elsewhere and we need to short-circuit
                    return;
                }
                const proxyTimeout = this.noSessionProxy.timeout;
                this.noSessionProxy.timeout = 1000;
                try {
                    currentStatus = await this.noSessionProxy.command('/status', 'GET');
                    if (currentStatus && currentStatus.ios && currentStatus.ios.ip) {
                        this.agentUrl = currentStatus.ios.ip;
                    }
                    this.log.debug(`WebDriverAgent information:`);
                    this.log.debug(JSON.stringify(currentStatus, null, 2));
                }
                catch (err) {
                    throw new Error(`Unable to connect to running WebDriverAgent: ${err.message}`);
                }
                finally {
                    this.noSessionProxy.timeout = proxyTimeout;
                }
            });
            // @ts-ignore processExited is a custom property
            if (this.xcodebuild.processExited) {
                // there has been an error elsewhere and we need to short-circuit
                return currentStatus;
            }
            this.log.debug(`WebDriverAgent successfully started after ${timer.getDuration().asMilliSeconds.toFixed(0)}ms`);
        }
        catch (err) {
            // at this point, if we have not had any errors from xcode itself (reported
            // elsewhere), we can let this go through and try to create the session
            this.log.debug(err.message);
            this.log.warn(`Getting status of WebDriverAgent on device timed out. Continuing`);
        }
        return currentStatus;
    }
    async quit() {
        await (0, utils_1.killProcess)('xcodebuild', this.xcodebuild);
    }
}
exports.XcodeBuild = XcodeBuild;
exports.default = XcodeBuild;
//# sourceMappingURL=xcodebuild.js.map