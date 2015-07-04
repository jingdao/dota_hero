package mechanics;

public class Ability {
	public static void assignAbility() {
		Hero.Earthshaker.skills.add(new Skill("Fissure",15,true,260,170) {
			public void effect(Hero target) {
				target.current_hp -= damage;
				target.statuses.add(new Status(Status.StatusType.STUN,2));
			}
		});
	} 
}
