let redis;
if (process.env.REDISTOGO_URL) {
    let rtg = require("url").parse(process.env.REDISTOGO_URL);
    redis = require("redis").createClient(rtg.port, rtg.hostname);
    
    redis.auth(rtg.auth.split(":")[1]);
} else {
    redis = require('redis').createClient();
}
redis.connect();
redis.on('connect', function() {
  console.log('Connected to redis instance');
}).on('error', function() {
    console.log('Error connecting to redis');
  });


async function updateIp(serverId, ip){
    console.log(`Updating address for ${serverId}...`)
    result = await redis.set(serverId, ip);
    if (result==="OK"){
       return true;
    } else{
        return false;
    }
}
    

async function getIp(serverId){
    console.log(`Retrieving ip for ${serverId}...`)
    result = await redis.get(serverId);
    if (!result){
        console.log("serverId not found!")
        return false;
    } else {
        return result;
    }
}

module.exports = {updateIp, getIp};

