package main.launchControllers;

import main.utils.GameLevel;

import static main.World.rand;

/**
 * A Periodic Stochastic Launch Condition.
 * After activation, it first waits for {@code initCooldown} steps, then commit the first launch (deterministically).
 * From then on, each stochastic launch will have a {@link #cooldown}-step cooldown.
 * At each stochastic launch, with probability {@link #prob} a launch event will be issued.
 *
 * @author 蔡辉宇
 */
public class PeriodicStochasticLaunchCondition implements LaunchCondition {
    int cooldown, curCooldown;
    float prob;
    boolean firstRelease = true;

    /**
     * Constructor.
     *
     * @param cooldown        Number of steps between each launch cycle after the first launch.
     * @param initCooldown    Number of steps between activation and first launch.
     * @param prob            Probability of a launch event being actually issued at each stochastic launch.
     */
    public PeriodicStochasticLaunchCondition(int cooldown, int initCooldown, float prob) {
        this.cooldown = cooldown;
        this.curCooldown = initCooldown;
        this.prob = prob;
    }

    @Override
    public boolean shouldLaunchNow() {
        --curCooldown;
        if (curCooldown > 0) {
            return false;
        }
        curCooldown = cooldown;
        if (firstRelease) {
            firstRelease = false;
            return true;
        }
        float randomFloat = rand.nextFloat();
        return randomFloat < prob;
    }

    public void scaleByGameLevel(GameLevel gameLevel, boolean increaseCooldown) {
        float[] coeffs = {0.7f, 1f, 1.3f, 1.6f};
        float coeff = coeffs[gameLevel.ordinal()];
        if (!increaseCooldown)
            coeff = 1 / coeff;
        cooldown = (int) (cooldown * coeff);
        curCooldown = (int) (curCooldown * coeff);
        prob /= coeff;
    }
}
