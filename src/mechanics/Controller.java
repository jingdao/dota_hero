package mechanics;
import java.util.ArrayList;
import java.util.Random;

public class Controller {

	ArrayList<Hero> my_team,enemy_team;
	String RED = "\033[0;31m";
	String GREEN = "\033[0;32m";
	String ORANGE = "\033[0;33m";
	String BLUE = "\033[0;34m";
	String PURPLE = "\033[0;35m";
	String CYAN = "\033[0;36m";
	String GRAY = "\033[0;37m";
	String YELLOW = "\033[1;33m";
	String WHITE = "\033[1;37m";
	String NORMAL = "\033[0m";
	Random random = new Random();

	public Controller() {
		my_team = new ArrayList<Hero>();
		my_team.add(Hero.Earthshaker);
		my_team.add(Hero.Lina);
		my_team.add(Hero.AntiMage);
		my_team.add(Hero.Doom);
		my_team.add(Hero.Tusk);
		enemy_team = new ArrayList<Hero>();
		enemy_team.add(Hero.Clinkz);
		enemy_team.add(Hero.Viper);
		enemy_team.add(Hero.Dazzle);
		enemy_team.add(Hero.Leshrac);
		enemy_team.add(Hero.Enigma);
		
		while (true) {
			double minTurn = 1000;
			double maxTurn = 0;
			boolean myturn = true;
			int maxIndex = 0;
			//increase turn
			for (int i=0;i<my_team.size();i++) {
				Hero h = my_team.get(i);
				h.turn += h.move_speed;
				if (h.turn > maxTurn) {
					maxTurn = h.turn;
					maxIndex = i;
					myturn = true;
				}
				if (h.turn < minTurn) minTurn = h.turn;
			}
			for (int i=0;i<enemy_team.size();i++) {
				Hero h = enemy_team.get(i);
				h.turn += h.move_speed;
				if (h.turn > maxTurn) {
					maxTurn = h.turn;
					maxIndex = i;
					myturn = false;
				}
				if (h.turn < minTurn) minTurn = h.turn;
			}
			if (myturn) {
				upkeep(my_team.get(maxIndex));
				displayBattlefield(maxTurn);
				getAction(maxIndex);
				my_team.get(maxIndex).turn = 0;
			} else {
				upkeep(enemy_team.get(maxIndex));
				getAIAction(maxIndex);
				enemy_team.get(maxIndex).turn = 0;
			}
			ArrayList<Hero> my_expired = new ArrayList<Hero>();
			ArrayList<Hero> enemy_expired = new ArrayList<Hero>();
			for (Hero h:my_team) {
				if (h.current_hp <= 0) {
					my_expired.add(h);
					System.out.println(h.name+" is killed");
				}
			}
			for (Hero h:enemy_team) {
				if (h.current_hp <= 0) {
					enemy_expired.add(h);
					System.out.println(h.name+" is killed");
				}
			}
			my_team.removeAll(my_expired);
			enemy_team.removeAll(enemy_expired);
			if (enemy_team.size() == 0) {
				System.out.println("You win!");
				break;
			} else if (my_team.size() == 0) {
				System.out.println("You lost!");
				break;
			}
		}
	}
	
	public void getAIAction(int heroIndex) {
		int target = random.nextInt(my_team.size());
		attack(enemy_team.get(heroIndex),my_team.get(target));
	}

	public void getAction(int heroIndex) {
		while (true) {
			String prompt = my_team.get(heroIndex).name+": attack";
			for (Skill sk:my_team.get(heroIndex).skills) {
				prompt += ", "+sk.name+"("+sk.currentCooldown+")";
			}
			prompt += ">>>";
			System.out.print(prompt);
			try { 
				Hero hr = my_team.get(heroIndex);
				int skillIndex = Integer.parseInt(System.console().readLine());
				if (skillIndex == 0) {
					getTarget(heroIndex,skillIndex);
					break;
				} else if (skillIndex <= hr.skills.size()) {
					Skill sk = hr.skills.get(skillIndex - 1);
					if (sk.currentCooldown == 0 && sk.mana <= hr.current_mana) {
						System.out.println(hr.name+" used "+sk.name);
						if (sk.requireTarget)
							getTarget(heroIndex,skillIndex);
						else
							sk.effect(null,enemy_team);
						hr.current_mana -= sk.mana;
						sk.currentCooldown = sk.cooldown;
						break;
					}
				}
			}
			catch (NumberFormatException e) {}
		}
	}
	
	public void getTarget(int heroIndex,int skillIndex) {
		while (true) {
			if (skillIndex == 0)
				System.out.print(my_team.get(heroIndex).name+": choose target (0..."+(enemy_team.size()-1)+")>>>");
			else
				System.out.print(my_team.get(heroIndex).skills.get(skillIndex-1).name+": choose target (0..."+(enemy_team.size()-1)+")>>>");
			try { 
				int targetIndex = Integer.parseInt(System.console().readLine());
				if (targetIndex >= 0 && targetIndex < enemy_team.size()) {
					if (skillIndex == 0) {
						attack(my_team.get(heroIndex),enemy_team.get(targetIndex));
					} else {
						Skill sk = my_team.get(heroIndex).skills.get(skillIndex - 1);
						sk.effect(enemy_team.get(targetIndex),enemy_team);
					} 
					break;
				}
			}
			catch (NumberFormatException e) {}
		}
	}
	
	public void attack(Hero h1, Hero h2) {
		System.out.println(h1.name+" attacks "+h2.name+" for "+h1.damage+" damage");
		h2.current_hp -= h1.damage;
	}

	public void upkeep(Hero h) {
		h.current_hp += h.hp_regen;
		if (h.current_hp > h.hp) h.current_hp = h.hp;
		h.current_mana += h.mana_regen;
		if (h.current_mana > h.mana) h.current_mana = h.mana;
		for (Skill sk:h.skills) {
			if (sk.currentCooldown > 0)
				sk.currentCooldown--;
		}
		ArrayList<Status> expired = new ArrayList<Status>();
		h.isStunned = false;
		h.isSleeped = false;
		h.isSilenced = false;
		h.isDisarmed = false;
		for (Status st:h.statuses) {
			st.duration--;
			if (st.duration != 0) {
				switch (st.type) {
					case STUN: h.isStunned = true; break;
					case SLEEP: h.isSleeped = true; break;
					case SILENCE: h.isSilenced = true; break;
					case DISARM: h.isDisarmed = true; break;
				}
			} else {
				expired.add(st);
			}
		}
		h.statuses.removeAll(expired);
	}
		
	public void displayBattlefield(double maxTurn) {
		displayHero(enemy_team,maxTurn);
		//System.out.println("\n");
		displayHero(my_team,maxTurn);
	}

	public void displayHero(ArrayList<Hero> list,double maxTurn) {
		String s = "";
		for (Hero h : list)
			s += String.format("%-11s ",h.name);
		s+="\n";
		for (Hero h: list) {
			String status = "";
			status += String.format("%2d%%",(int) (100.0*h.turn/(maxTurn+1)));
			for (Status st: h.statuses) {
				switch (st.type) {
					case STUN: status += ORANGE + "T"; break;
					case SLOW: status += YELLOW + "W"; break;
					case SLEEP: status += BLUE + "E"; break;
					case SILENCE: status += GRAY + "S"; break;
					case DISARM: status += WHITE + "D"; break;
					case POISON: status += PURPLE + "P"; break;
				}
			}
			status += NORMAL;
			s += status;
			for (int i=0;i<12 - 3 - h.statuses.size();i++)
				s += " ";
		}
		s+="\n";
		for (Hero h : list) {
			if (h.current_hp <= 0)
				s += String.format("%s%5d/%5d%s ",WHITE,0,(int)h.hp,NORMAL);
			else if (h.current_hp >= h.hp / 2)
				s += String.format("%s%5d/%5d%s ",GREEN,(int)h.current_hp,(int)h.hp,NORMAL);
			else
				s += String.format("%s%5d/%5d%s ",RED,(int)h.current_hp,(int)h.hp,NORMAL);
		}
		s+="\n";
		for (Hero h : list)
			s += String.format("%s%5d/%5d%s ",CYAN,(int)h.current_mana,(int)h.mana,NORMAL);
		s+="\n";
		System.out.println(s);
	}

	public static void main(String[] args) {
		Ability.assignAbility();
		Controller c = new Controller();
	}
}
