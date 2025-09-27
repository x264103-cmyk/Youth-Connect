@echo off
echo 测试Maven依赖解析...

echo.
echo 检查父工程...
mvn -f pom.xml validate

echo.
echo 检查网关服务...
mvn -f gateway-service/pom.xml validate

echo.
echo 检查用户服务...
mvn -f user-service/pom.xml validate

echo.
echo 检查认证服务...
mvn -f auth-service/pom.xml validate

echo.
echo 检查聊天服务...
mvn -f chat-service/pom.xml validate

echo.
echo 检查AI审核服务...
mvn -f ai-audit-service/pom.xml validate

echo.
echo 检查动态服务...
mvn -f post-service/pom.xml validate

echo.
echo 检查活动服务...
mvn -f event-service/pom.xml validate

echo.
echo 检查消息服务...
mvn -f message-service/pom.xml validate

echo.
echo 检查推荐服务...
mvn -f recommend-service/pom.xml validate

echo.
echo 所有服务依赖检查完成！
pause
