# Teq-UI-Automation

### Setup project:

- Clone git repo using `https://github.com/faizulcse/mobile-automation.git`
- Install **node.js** [download](https://nodejs.org/en/download/) link
- Install **appium** using npm command `npm install -g --force appium`

**Add ENV variables:**

- `export JAVA_HOME=/Users/username/Library/Android/sdk`
- `export ANDROID_HOME=/Users/username/Library/Android/sdk`

**Add PATH ENV variables:**

- `export PATH=$PATH:$ANDROID_HOME/platform-tools`
- `export PATH=$PATH:$ANDROID_HOME/tools/bin`
- `export PATH=$PATH:$ANDROID_HOME/tools`

### Run Tests:

- Before run tests **APK** or **IPA** file should be placed in the project root dir
- From `src/test/resources/app-config.properties` rename `APP_TYPE=android` or `APP_TYPE=ios`
- Open terminal
- Go to project root directory
- Run `mvn clean test`
