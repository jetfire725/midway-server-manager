let iptable = {theatrejesus: "192.168.50.234"};

function updateIp(serverId, ip){
    console.log(`Updating address for ${serverId}...`)
    iptable[serverId]=ip;
    return true;
}

function getIp(serverId){
    console.log(`Retrieving ip for ${serverId}...`)
    if (serverId in iptable){
        return iptable[serverId];
    } else {
        return false;
    }
}

module.exports = {updateIp, getIp};

