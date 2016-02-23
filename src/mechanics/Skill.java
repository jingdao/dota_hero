package mechanics;
import java.util.ArrayList;

public class Skill {
	public String name;
	public int cooldown;
	public int currentCooldown;
	public boolean requireTarget;
	public int damage;
	public int mana;
	public double chance;

	public Skill(String name,int cooldown,boolean requireTarget,int damage,int mana,double chance) {
		this.name = name;
		this.cooldown =cooldown;
		this.currentCooldown = 0;
		this.requireTarget = requireTarget;
		this.damage = damage;
		this.mana = mana;
		this.chance = chance;
	}

	public void effect(Hero target, ArrayList<Hero> team) {

	}
}
