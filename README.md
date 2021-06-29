# MSc-S2-Alternative-User-Interface-Engineering-TypeTrainer

Application to practice your typing.
After a typing session you are provided with results and statistics.

An experimental version of a hand tracking feature is included that requires the
companion Android app. The hand tracking feature allows you to get an indication
if the correct finger was used to press the key. The feature is quite unstable though.

## Build Instructions:

### Desktop:
1. Build and Run:
```shell
gradle desktop:run
```
2. Jar Build
```shell
gradle desktop:packageUberJarForCurrentOS
```
3. Exe Build
```shell
gradle desktop:packageExe
```
4. Dmg Build (Not officially supported):
```shell
gradle desktop:packageDmg
```

### Android:

1. Build and Run:
```shell
gradle android:installDebug
```
