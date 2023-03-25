/*
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 * 
 */

// Write your code here

package com.example.player.service;
// we are importing  repository to implement it 
import com.example.player.repository.PlayerRepository;
// we are importing  book to ude getters and setters 
import com.example.player.model.Player;
import java.util.*;
// we are importing the jdbc templests and autowired injections 
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
// we are importing the sql based commands and to use the rowmapper
import com.example.player.model.PlayerRowMapper;



import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@Service
public class PlayerH2Service implements PlayerRepository 
{

  @Autowired
  private JdbcTemplate db;
@Override
 public ArrayList<Player> getPlayers() {
  
    List<Player> playerList = db.query("select * from team", new PlayerRowMapper());
        ArrayList<Player> players = new ArrayList<>(playerList);
    return players;
  }


  @Override
  public Player getPlayerById(int playerId) {
    try{
    Player player = db.queryForObject("select * from team where playerId = ?", 
new PlayerRowMapper(), playerId); 
    return player;
    }
    catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }


  @Override
  public Player addPlayer(Player player) {
     db.update("insert into team(playerName,jerseyNumber,role) values (?,?,?)", 
									player.getPlayerName(), player.getJerseyNumber(),player.getRole());
                  Player savedPlayer = db.queryForObject(
"select * from team where playerName = ? and jerseyNumber = ? and role = ? ",
new PlayerRowMapper(), player.getPlayerName(), player.getJerseyNumber(), player.getRole());
    return savedPlayer;

  }


  @Override
  public Player updatePlayer(int playerId, Player player) {
   if(player.getPlayerName() != null) {
      db.update("update team set playerName= ? where playerId = ?",   
       					player.getPlayerName(), playerId);
    }
if(player.getJerseyNumber() != 0){
 db.update("update team set jerseyNumber = ? where playerid = ?", 
					player.getJerseyNumber(), playerId);
    }
    if(player.getRole() != null){
 db.update("update team set role = ? where playerId = ?", 
					player.getRole(), playerId);
    }
    return getPlayerById(playerId);
  }

 @Override
  public void deletePlayer(int playerId) {
     db.update("delete from team where playerId = ?", playerId);

      
  }







}