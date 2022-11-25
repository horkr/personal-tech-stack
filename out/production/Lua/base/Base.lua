--start

--Lua的数字只有double型，64bits
customNum = 1000
print(customNum)
print('------------------------------end----------------------------------')
--字符串
text = 'xxx'
print(text)
print('------------------------------end----------------------------------')
--C语言中的NULL在Lua中是nil，比如你访问一个没有声明过的变量，就是nil
print(kkk)
print('------------------------------end----------------------------------')

--变量前加local关键字的是局部变量。没有特殊说明，全是全局变量
local var1 = 'sss'
print(var1)

print('------------------------------end----------------------------------')
--while
local i = 0
local max = 10
while i <= max
do
    print(i)
    i = i + 1
end
print('------------------------------end----------------------------------')
--函数
local function m1(age)
    local sex = "Male"
    if age == 40 and sex == "Male" then
        print(" 男人四十一枝花 ")
    else
        if age > 60 and sex ~= "Female" then
            print("old man without country!")
        elseif age < 20 then
            io.write("too young, too naive!\n")
        else
            print("Your age is " .. age)
        end
    end
end

m1(40)
print('------------------------------end----------------------------------')

--for 循环
for i = 10, 1, -1
do
    print(i)
end
print('------------------------------end----------------------------------')

function myPower(x, y)
    return y + x
end
power2 = myPower(2, 3)
print(power2)

print('------------------------------end----------------------------------')
function newCounter()
    local i = 0
    return function()
        -- 匿名函数
        i = i + 1
        return i
    end
end
--这里c1是一个函数
c1 = newCounter()
print(c1)
print(c1())

print('------------------------------end----------------------------------')


--变量和返回值可以批量操作
name, age, bGay = "yiming", 37, false, "yimingl@hotmail.com"
print(name, age, bGay)

function isMyGirl(name)
    return name == 'xiao6', name
end
local bol, name = isMyGirl('xiao6')

print(name, bol)
print('------------------------------end----------------------------------')

--Table key，value的键值对 类似 map
lucy = { name = 'titi', age = 18, height = 165.5 }
lucy.age = 35
print(lucy.name, lucy.age, lucy.height)

print(lucy)

print('------------------------------end----------------------------------')
--数组
arr = {"string", 100, "xiao6",function() print("memeda") return 1 end}
print(arr[0])
print(arr[4]())

--遍历
for k, v in pairs(arr) do print(k, v)
end

print('------------------------------end----------------------------------')

--面向对象
person = {name='kruo',age = 18}

function  person.eat(food)

    print(person.name .." eating "..food)

end
person.eat("xxoo")

