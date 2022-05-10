# voice-tts 
Android离线语音合成   
[![](https://jitpack.io/v/cczhr/voice-tts.svg)](https://jitpack.io/#cczhr/voice-tts)

# 说明
将Releases里面的lib.zip里面的so 放到项目lib路径中  
在Project 的 build.gradle 加上

```groovy
repositories {
    ...
	maven { url 'https://jitpack.io' }
}
```

在Module的 build.gradle 加上

```groovy
android {
    ndk {  
        abiFilters  'armeabi' 
    }
    sourceSets {
        main {
            jniLibs.srcDirs = ['libs']
        }
    }
}
dependencies {
    ...
	 implementation 'com.github.cczhr:voice-tts:1.0.1'
}
```

# 简单使用
```java
TTS tts=TTS.getInstance();//获取单例对象
tts.init(this);//初始化
tts.speakText("这是一条测试语音");//语音合成
tts.isSpeaking();//是否正在播放中
//在程序结束时调用
tts.release();//释放资源
```
更多用法参照 `TTS.java` `TTSConstants.java`
