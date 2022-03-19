const express   = require('express')
const database = require('./dao.js')
// var proxy = require("node-tcp-proxy");
// const request = require('request');
const app = express();
const port = process.env.PORT || 3000;

app.use(express.json())
app.use('/downloads', express.static('downloads'))

//UPDATE DATABASE IP TABLE
app.post('/update/ip', (req, res) => {
    const serverId = req.body.serverId;
    const ip = req.body.ip;
    let status = database.updateIp(serverId, ip)
    if (status){
      res.sendStatus(200)  
    }
    
});

app.post('/get/ip', (req, res) => {
    const serverId = req.body.serverId;
    let ip = database.getIp(serverId);
    res.send(ip);
    
});

app.listen(port, ()=>{
    console.log(`Listening on port ${port}...`)
});

//var newProxy = proxy.createProxy(25565, "192.168.50.100", 25565);



