<div style='align:center'>
  <h1>qHttp</h1>
</div>

纯手写实现的一个轻便爬虫库，语法糖效仿WinHttp，易语言出身的朋友可能会很喜欢这个库。

除了异步外其他该有的功能都有，个人感觉比OkHttp好用，可能是因为用了很多年的WinHttp，有语法情怀的原因。

## 函数说明

AutoCookie()：自动储存请求Response中的Set-Cookie

setTimeout(int connectTime, int readTime) 设置连接超时和读取超时时间（毫秒为单位）

setProxy(String host, int port) / setProxy(String host, int port, String username, String password)  设置代理IP

forbidProxy()  取消代理IP

setHeader(String key, String value) 设置请求头

removeHeader(String key) 删除请求头

setContentType(String contentType) 设置Content-Type

setUserAgent(String userAgent) 设置UserAgent

open(String urlPath) 创建url连接

get()  发送get请求，返回qHttpResponse

post() / post(String params) 发送post请求，返回qHttpResponse

setCookies(String cookieAll) 批量设置Cookies

## 使用演示

```
qHttp http = new qHttp();
//获取百度数据
http.open("https://www.baidu.com");
qHttpResponse httpResponse = http.get();
System.out.println(httpResponse.getBody());
```

代码中附赠一个该库实战项目使用案例

秒刷网课（安全云课堂）
