function check(){
    var x = Player.getCurrentTileX();
    var y = Player.getCurrentTileY();
    if(Environment.getWorld().map.getTile(x, y, 0).id == ID.get("block:tree")) return true;
    return false;
}