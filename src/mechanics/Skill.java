package mechanics;

public class Skill {
	public String name;
	public int cooldown;
	public int currentCooldown;
	public boolean requireTarget;
	public int damage;
	public int mana;

	public Skill(String name,int cooldown,boolean requireTarget,int damage,int mana) {
		this.name = name;
		this.cooldown =cooldown;
		this.currentCooldown = 0;
		this.requireTarget = requireTarget;
		this.damage = damage;
		this.mana = mana;
	}

	public void effect(Hero target) {

	}
}
