cd "D:\Work_Space\ims-gateway-ms"

mvn -q dependency:build-classpath -Dmdep.outputFile="dev-tools\PasswordEncryptor\classpath.txt"

$cp = Get-Content "dev-tools\PasswordEncryptor\classpath.txt" -Raw

javac -cp "$cp" -d "dev-tools\PasswordEncryptor" "dev-tools\PasswordEncryptor\BcryptDemo.java"

java -cp "dev-tools\PasswordEncryptor;$cp" BcryptDemo "my password"