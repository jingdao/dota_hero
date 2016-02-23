package mechanics;
import java.util.ArrayList;
import java.util.Random;

public class Ability {
	public static void assignAbility() {
		final Random random = new Random();
		Hero.Earthshaker.skills.add(new Skill("Fissure",3,true,260,170,0.2) {
			public void effect(Hero target,ArrayList<Hero> team) {
				target.current_hp -= damage;
				target.statuses.add(new Status(Status.StatusType.STUN,2));
				System.out.println(target.name+" is stunned and takes "+damage+" damage");
				for (Hero h:team) {
					if (h != target && random.nextDouble() < chance) {
						h.current_hp -= damage;
						h.statuses.add(new Status(Status.StatusType.STUN,2));
						System.out.println(h.name+" is stunned and takes "+damage+" damage");
					}
				}
			}
		});
		Hero.Earthshaker.skills.add(new Skill("Enchant Totem",1,false,0,50,0.1){
			public void effect(Hero target,ArrayList<Hero> team) {
				System.out.println("Earthshaker gains 400% damage");
				for (Hero h:team) {
					if (random.nextDouble() < chance) {
						h.statuses.add(new Status(Status.StatusType.STUN,1));
						System.out.println(h.name+" is stunned");
					}
				}
			}
		});
		Hero.Earthshaker.skills.add(new Skill("Echo Slam",20,false,270,265,0.6){
			public void effect(Hero target,ArrayList<Hero> team) {
				ArrayList<Hero> affected = new ArrayList<Hero>();
				for (Hero h:team) {
					if (random.nextDouble() < chance) {
						affected.add(h);
					}
				}
				double total_damage = damage + (affected.size()-1) * 70;
				for (Hero h:affected) {
					h.current_hp -= total_damage;
					h.statuses.add(new Status(Status.StatusType.STUN,1));
					System.out.println(h.name+" is stunned and takes "+total_damage+" damage");
				}
			}
		});
	} 
}
