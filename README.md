# MSc-S2-Alternative-User-Interface-Engineering-TypeTrainer

Application to practice your typing.
After a typing session you are provided with results and statistics.

An experimental version of a hand tracking feature is included that requires the
companion Android app. The hand tracking feature allows you to get an indication
if the correct finger was used to press the key. The feature is quite unstable though.

## Build Instructions:

### Documentation:
The latest dokka documentation can be found at:<br> https://jan222ik.github.io/MSc-S2-Alternative-User-Interface-Engineering-TypeTrainer/
<br>Otherwise, the documentation can be generated with the following gradle task.
```shell
gradle dokkaHTMLWithCustomCss
```
The generated html can be found at ``/build/dokkaCustomMultiModuleOutput/index.html``

### Desktop:
1. Build and Run:
```shell
gradle desktop:run
```
2. Run as Distributable:
```shell
gradle desktop:runDistributable
```
3. Exe Build
```shell
gradle desktop:packageExe
```
4. Dmg Build (Not officially supported):
```shell
gradle desktop:packageDmg
```
5. Args:<br>
The following arguments are available for the application:
* Debug Mode:``-Ddebug=true``<br>
* Username Mode: ``-Duser="<username>"``


### Android:

1. Build and Run:
```shell
gradle android:installDebug
```
