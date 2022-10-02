package ml.windleaf.blockracing.events;

import ml.windleaf.blockracing.entity.goals.GoalBlock;
import ml.windleaf.blockracing.team.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import javax.annotation.Nonnull;

public class PlayerCompleteGoalEvent extends PlayerEvent {
  private static final HandlerList handlers = new HandlerList();
  private final Team team;
  private final GoalBlock goal;

  public PlayerCompleteGoalEvent(Player player, Team team, GoalBlock goal) {
    super(player);
    this.team = team;
    this.goal = goal;
  }

  @Override
  @Nonnull
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }

  public Team getTeam() {
    return team;
  }

  public GoalBlock getGoal() {
    return goal;
  }
}
