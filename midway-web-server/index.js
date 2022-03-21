const express   = require('express')
const database = require('./dao.js')
// var proxy = require("node-tcp-proxy");
// const request = require('request');
const app = express();
const port = process.env.PORT || 3000;

app.use(express.json())
app.use('/downloads', express.static('downloads'))


//UPDATE SERVER IP
app.post('/update/ip', (req, res) => {
    let serverId = req.body.serverId;
    let ip = req.body.ip;
    database.updateIp(serverId, ip).then(status =>{
       res.send(status) 
    })
    
    
});

//GET IP
app.post('/get/ip',(req, res) => {
    let serverId = req.body.serverId;
    database.getIp(serverId).then(result =>{
        res.send(result);
    })
    
    
});

app.listen(port, ()=>{
    console.log(`Listening on port ${port}...`)
});

//var newProxy = proxy.createProxy(25565, "192.168.50.100", 25565);



