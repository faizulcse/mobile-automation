# Teq-UI-Automation

### Setup project:

- Clone git repo using `git clone https://github.com/busgroup/teq-ui-automation.git`
- Install **node.js** download [link](https://nodejs.org/en/download/)
- Install **appium** using npm command `npm install -g --force appium`

**Add ENV variables:**

- `export JAVA_HOME=/Users/username/Library/Android/sdk`
- `export ANDROID_HOME=/Users/username/Library/Android/sdk`

**Add PATH ENV variables:**

- `export PATH=$PATH:$ANDROID_HOME/platform-tools`
- `export PATH=$PATH:$ANDROID_HOME/tools/bin`
- `export PATH=$PATH:$ANDROID_HOME/tools`

### Run Tests:

- **APK** or **IPA** file should be placed in the project root dir before run tests
- Open terminal
- Go to project root directory
- Run `mvn clean test`
