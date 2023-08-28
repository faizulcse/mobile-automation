# Mobile-Automation

### Setup project:

- Clone git repo using `https://github.com/faizulcse/mobile-automation.git`
- Install **node.js** [download](https://nodejs.org/en/download/) link
- Install **appium** using npm command `npm install -g --force appium`
- Install **uiautomator2** using npm command `appium driver install uiautomator2`
- Install **xcuitest** using npm command `appium driver install xcuitest`

**Setup macOS Environment and PATH:**

- `export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.jdk/Contents/Home`
- `export ANDROID_HOME=/Users/$USER_NAME/Library/Android/sdk`
- `export PATH=$PATH:$ANDROID_HOME/platform-tools`
- `export PATH=$PATH:$ANDROID_HOME/tools/bin`
- `export PATH=$PATH:$ANDROID_HOME/tools`

### Run Tests:

- Before run tests **APK** or **IPA** file should be placed in the project root dir
- Update Android app config: `src/test/resources/android.properties`
- Update iOS app config: `src/test/resources/ios.properties`
- Open terminal
- Go to project root directory
- Run android app: `mvn clean test`
- Run iOS app: `mvn clean test -DIOS=true`
- Run android suite file **testng.xml**: `mvn clean test -Dsuitefile=testng.xml`
- Run iOS suite file **testng.xml**: `mvn clean test -DIOS=true -Dsuitefile=testng.xml`
