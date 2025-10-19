
package model.management;


import java.util.List;

import model.board.IZone;
import model.management.interfaces.IMediator;
import model.players.IPlayer;

public abstract class Mediator implements IMediator {

	// Metodi per accedere ai dati del MapManager
    public abstract List<IZone> getAllZones();
    public abstract List<IZone> getZonesOwnedBy(IPlayer player);
    public abstract boolean canMoveBetween(IPlayer player, IZone fromZone, IZone toZone);
}
