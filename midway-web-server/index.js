const express   = require('express')
const database = require('./dao.js')
var proxy = require("node-tcp-proxy");
const app = express();
const port = process.env.PORT || 3000;

app.use(express.json())

//UPDATE DATABASE IP TABLE
app.post('/update/ip', (req, res) => {
    const user = req.body.user;
    const oldip = req.body.oldip;
    const newip = req.body.newip;
    status = database.updateip(user, oldip, newip)
    res.sendStatus(200)
});

app.listen(port, ()=>{
    console.log(`Listening on port ${port}...`)
});

var newProxy = proxy.createProxy(25565, "192.168.50.100", 25565);



