package raidenObjects.bonus;

import motionControllers.ConstSpeedTargetTrackingMotionController;
import raidenObjects.BaseRaidenObject;
import raidenObjects.aircrafts.shootingAircrafts.PlayerAircraft;
import utils.Faction;

import java.io.File;
import java.nio.file.Paths;

import static world.World.player1;
import static world.World.player2;

/**
 * Base class for all bonuses.
 * Bonus is a small floating object that, once picked up by a player, gives it additional strength or abilities.
 * All bonuses are controlled by an {@link motionControllers.XYMotionController}, which consists of a
 * {@link motionControllers.HoveringXMotionController} and a {@link motionControllers.ConstSpeedYMotionController},
 * which makes the bonus hover around, waiting to be picked up.
 *
 * @author 张哲瑞
 */
public abstract class BaseBonus extends BaseRaidenObject {
    /**
     * Most bonuses (except {@link WeaponUpgradeBonus}) can be attracted by the player's magnet.
     *
     * @see PlayerAircraft#getMagnetCountdown()
     * @see MagnetBonus
     */
    boolean attracted = false;

    /**
     * Constructor.
     *
     * @param name     Name of the bonus.
     * @param imgSizeX Image width.
     * @param imgSizeY Image height.
     * @param faction  Faction of the bonus, usually {@link Faction#BONUS}.
     */
    protected BaseBonus(String name, int imgSizeX, int imgSizeY, Faction faction) {
        super(name, imgSizeX, imgSizeY, faction);
    }

    public boolean isAttracted() {
        return attracted;
    }

    public void becomesAttracted() {
        attracted = true;
    }

    /**
     * Logic for the effect of this bonus.
     * @param aircraft The aircraft receiving this bonus.
     */
    public abstract void bonus(PlayerAircraft aircraft);

    /**
     * Interact with {code aircraft}.
     * @param aircraft An aircraft interacting with this bonus.
     */
    public void interactWith(PlayerAircraft aircraft) {
        if (!(this.isAlive() && aircraft.isAlive()))
            return;

        // if bonus hits the player
        if (this.hasHit(aircraft)) {
            this.bonus(aircraft);
            this.markAsDead();
        }
        // if player is attractive (has magnet) and the bonus hasn't been attracted
        if (aircraft.getMagnetCountdown().isEffective() && !this.isAttracted()) {
            becomesAttracted();
            if (isAttracted())
                this.registerMotionController(new ConstSpeedTargetTrackingMotionController(aircraft, 0.3f, 10f));
        }
    }

    /**
     * Mark this aircraft as dead.
     * There is no visual effect for {@link BaseBonus} and its children,
     * so we make it disappear from the screen immediately.
     */
    @Override
    public void markAsDead() {
        super.markAsDead();
        becomeInvisible();
    }

    /**
     * Return the image file.
     * The file name starts with {@code name}, and ends with suffix ".png".
     * Note: The image files are all stored in "data/images".
     *
     * @return A File object representing current image of this bonus object.
     */
    public File getImageFile() {
        return Paths.get("data", "images", getName() + ".png").toFile();
    }

    /**
     * This function combines all control logic for this bonus.
     * Note: It can be overridden by children if additional logic is needed.
     */
    public void step() {
        if (isAlive()) {
            getMotionController().scheduleSpeed();
            move();
        }
        if (isAlive()) {
            if (player1 != null)
                interactWith(player1);
            if (player2 != null)
                interactWith(player2);
        }
    }
}
