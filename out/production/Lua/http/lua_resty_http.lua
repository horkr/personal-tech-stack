local http = require("http")

local httpc = http.new()  
  
local resp,err = httpc:request_uri("http://www.sogou.com", {  
    method = "GET",  
    path = "/sogou?query=resty.http",  
    headers = {  
        ["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36"  
    }  
})  
  
if not resp then  
    print("request error :", err)
    return  
end  
  
 
ngx.status = resp.status  
  
  
for k, v in pairs(resp.headers) do  
    if k ~= "Transfer-Encoding" and k ~= "Connection" then  
        ngx.header[k] = v  
    end  
end

print.say(resp.body)
  
httpc:close()