export default XcodeBuild;
export class XcodeBuild {
    /**
     * @param {string} xcodeVersion
     * @param {any} device
     * @param {any} args
     * @param {import('@appium/types').AppiumLogger?} log
     */
    constructor(xcodeVersion: string, device: any, args?: any, log?: import('@appium/types').AppiumLogger | null);
    xcodeVersion: string;
    device: any;
    log: import("@appium/types").AppiumLogger;
    realDevice: any;
    agentPath: any;
    bootstrapPath: any;
    platformVersion: any;
    platformName: any;
    iosSdkVersion: any;
    showXcodeLog: any;
    xcodeConfigFile: any;
    xcodeOrgId: any;
    xcodeSigningId: any;
    keychainPath: any;
    keychainPassword: any;
    prebuildWDA: any;
    usePrebuiltWDA: any;
    useSimpleBuildTest: any;
    useXctestrunFile: any;
    launchTimeout: any;
    wdaRemotePort: any;
    updatedWDABundleId: any;
    derivedDataPath: any;
    mjpegServerPort: any;
    prebuildDelay: any;
    allowProvisioningDeviceRegistration: any;
    resultBundlePath: any;
    resultBundleVersion: any;
    init(noSessionProxy: any): Promise<void>;
    noSessionProxy: any;
    xctestrunFilePath: string | undefined;
    retrieveDerivedDataPath(): Promise<any>;
    _derivedDataPathPromise: Promise<any> | undefined;
    reset(): Promise<void>;
    prebuild(): Promise<void>;
    xcodebuild: SubProcess | null | undefined;
    cleanProject(): Promise<void>;
    getCommand(buildOnly?: boolean): {
        cmd: string;
        args: string[];
    };
    createSubProcess(buildOnly?: boolean): Promise<SubProcess>;
    start(buildOnly?: boolean): Promise<any>;
    waitForStart(timer: any): Promise<null>;
    agentUrl: any;
    quit(): Promise<void>;
}
import { SubProcess } from 'teen_process';
//# sourceMappingURL=xcodebuild.d.ts.map