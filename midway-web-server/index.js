const express   = require('express')
const database = require('./dao.js')
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