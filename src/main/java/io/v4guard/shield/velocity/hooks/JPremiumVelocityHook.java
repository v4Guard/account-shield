package io.v4guard.shield.velocity.hooks;

import com.jakub.jpremium.velocity.api.User;
import com.jakub.jpremium.velocity.api.event.UserEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.proxy.Player;
import io.v4guard.shield.core.auth.AuthType;
import io.v4guard.shield.core.auth.Authentication;
import io.v4guard.shield.core.hook.AuthenticationHook;
import io.v4guard.shield.core.v4GuardShieldCore;
import io.v4guard.shield.velocity.v4GuardShieldVelocity;

import java.util.Optional;

public class JPremiumVelocityHook extends AuthenticationHook {

    private v4GuardShieldVelocity plugin;

    public JPremiumVelocityHook(v4GuardShieldVelocity plugin) {
        super("JPremium");
        this.plugin = plugin;
        plugin.getServer().getEventManager().register(plugin, this);
        plugin.sendConsoleMessage("§c[v4guard-account-shield] (Velocity) Hooked into JPremium");
    }

    @Subscribe
    public void onUserEvent(UserEvent.Login event) {
        Optional<Player> player = plugin.getServer().getPlayer(event.getUserProfile().getUniqueId());
        if(player.isPresent()){
            Player proxiedPlayer = player.get();
            Authentication auth = new Authentication(
                    proxiedPlayer.getUsername(),
                    AuthType.LOGIN,
                    proxiedPlayer.hasPermission("v4guard.accshield")
            );
            v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
        }

    }

    @Subscribe
    public void onUserEvent(UserEvent.Register event) {
        Optional<Player> player = plugin.getServer().getPlayer(event.getUserProfile().getUniqueId());
        User user = event.getUserProfile();
        //Ignore premium and bedrock players
        //JPremium will call UserEvent.Login for them
        if(user.isPremium() || user.isBedrock()) return;
        if(player.isPresent()){
            Player proxiedPlayer = player.get();
            Authentication auth = new Authentication(
                    proxiedPlayer.getUsername(),
                    AuthType.REGISTER,
                    proxiedPlayer.hasPermission("v4guard.accshield")
            );
            v4GuardShieldCore.getInstance().getMessager().sendMessage(auth);
        }
    }

}
