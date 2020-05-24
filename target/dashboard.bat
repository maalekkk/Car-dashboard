@ECHO OFFmode con: cols=145 lines=50

if not DEFINED IS_MINIMIZED set IS_MINIMIZED=1 && start "" /min "%~dpnx0" %* && exit

-Dprism.dirtyopts=false

"C:\Program Files\Java\jdk-11.0.7\bin\java.exe" --module-path "C:\Program Files\Java\javafx-sdk-11.0.2\lib" --add-modules javafx.base,javafx.graphics,javafx.media,javafx.controls,javafx.fxml --add-opens javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED --add-opens javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED --add-opens javafx.base/com.sun.javafx.binding=ALL-UNNAMED --add-opens javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED --add-opens javafx.base/com.sun.javafx.event=ALL-UNNAMED -jar ".\Dashboard.jar"
exit
