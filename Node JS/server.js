var http = require("http");
var express = require("express");
var bodyParser = require("body-parser");
var app = express();
const child_process = require("child_process");
app.use(bodyParser.json());
app.set('port', process.env.PORT || 3000);

app.post('/', function (req, res) {
    
            var content = req.body.content;
            var rate = req.body.rate;
            var method = req.body.method;
            const exec = child_process.exec("python mo.py " + method + " " + rate + " " + "\"" +  content + "\"" , function(error, stdout, stderr) {
            
                if (error) 
                res.send(error);
        
                else if (stderr) 
                res.send(stderr);
        
                else {
                    console.log("Gelen veriler = method : " + method);
                    console.log("Gelen veriler = rate : " + rate);
                    console.log("Gelen veriler = content: "+ content);
                    console.log("Python dönen sonuç = stdout : " + stdout);
                    res.send(stdout);
                }
        });
});

http.createServer(app).listen(app.get('port'), function () {
    console.log('Sunucu şu portu dinliyor :  ' + app.get('port'));
});