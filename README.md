# VerificationCodeView
Android 自定义方形输入框，仿滴滴验证码、小篮单车验证码输入框

##效果图
![Screenshot](screenshot/screenshot.gif)

GIF被压缩的有点模糊，下面是截图

![Screenshot](screenshot/screenshot_1.jpg)

---
##用法
所有属性

```
    <com.jyn.vcview.VerificationCodeView
        android:id="@+id/verificationcodeview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="30dp"
        android:gravity="center"
        android:orientation="horizontal"
        app:vcv_et_bg="@drawable/et_login_code"
        app:vcv_et_cursor="@drawable/et_cursor"
        app:vcv_et_inputType="number"
        app:vcv_et_number="4"
        app:vcv_et_text_color="@android:color/black"
        app:vcv_et_text_size="6sp" />
```
输入完成监听
```
verificationcodeview.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onComplete(String content) {
                textView.setText(content);
            }
        });
```
所有属性
```
<declare-styleable name="vericationCodeView">
        <!--输入框的数量-->
        <attr name="vcv_et_number" format="integer" />
        <!--输入类型-->
        <attr name="vcv_et_inputType">
            <enum name="number" value="0" />
            <enum name="numberPassword" value="1" />
            <enum name="text" value="2" />
            <enum name="textPassword" value="3" />
        </attr>
        <!--输入框的宽度-->
        <attr name="vcv_et_width" format="dimension|reference" />
        <!--输入框文字颜色-->
        <attr name="vcv_et_text_color" format="color|reference" />
        <!--输入框文字大小-->
        <attr name="vcv_et_text_size" format="dimension|reference" />
        <!--输入框背景-->
        <attr name="vcv_et_bg" format="reference" />
        <!--光标样式-->
        <attr name="vcv_et_cursor" format="reference" />
    </declare-styleable>
```
vcv_et_bg 背景示例（默认）
```
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">

    <item android:state_window_focused="false">
        <shape android:shape="rectangle">
            <solid android:color="#FFFFFF" />
            <stroke android:width="1dp" android:color="#C4C4C4" />
            <corners android:radius="5dp" />
        </shape>
    </item>

    <item android:state_focused="true">
        <shape android:shape="rectangle">
            <solid android:color="#ffffff" />
            <stroke android:width="1dp" android:color="#0dbc75" />
            <corners android:radius="5dp" />
        </shape>
    </item>
</selector>
```
vcv_et_cursor 光标示例（默认）
```
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <size android:width="2dp" />
    <solid android:color="#0dbc75" />
</shape>
```


## Gradle
Step 1. Add it in your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Step 2. Add the dependency

```
dependencies {
        compile 'com.jyn.verificationcodeview:verificationcodeview:1.0.0'

}
```