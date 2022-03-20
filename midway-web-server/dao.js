let redis;
if (process.env.REDIS_HOST & process.env.REDIS_PORT & process.env.REDIS_PASSWORD) {
    redis = require("redis").createClient(
        { 
        socket: {
            port: process.env.REDIS_PORT,
            host: process.env.REDIS_HOST
        },
        password: process.env.REDIS_PASSWORD
    })
    
} else {
    redis = require("redis").createClient()
}
redis.connect()

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

