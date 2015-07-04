package mechanics;
import java.util.ArrayList;

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
		
		displayBattlefield();
		int turn = 0;
		while (true) {
			getAction(turn);
			turn = (turn + 1) % 5;
		}
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
					displayBattlefield();
					break;
				} else if (skillIndex <= hr.skills.size()) {
					Skill sk = hr.skills.get(skillIndex - 1);
					if (sk.currentCooldown == 0 && sk.mana <= hr.current_mana) {
						if (sk.requireTarget)
							getTarget(heroIndex,skillIndex);
						else
							sk.effect(null);
						System.out.println(hr.name+" used "+sk.name);
						hr.current_mana -= sk.mana;
						sk.currentCooldown = sk.cooldown;
						displayBattlefield();
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
				System.out.print(my_team.get(heroIndex).name+": choose target (0...4)>>>");
			else
				System.out.print(my_team.get(heroIndex).skills.get(skillIndex-1).name+": choose target (0...4)>>>");
			try { 
				int targetIndex = Integer.parseInt(System.console().readLine());
				if (targetIndex >= 0 && targetIndex <= 4 && enemy_team.get(targetIndex).current_hp > 0) {
					if (skillIndex == 0) {
						attack(my_team.get(heroIndex),enemy_team.get(targetIndex));
					} else {
						Skill sk = my_team.get(heroIndex).skills.get(skillIndex - 1);
						sk.effect(enemy_team.get(targetIndex));
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
	
	public void displayBattlefield() {
		displayHero(enemy_team);
		//System.out.println("\n");
		displayHero(my_team);
	}

	public void displayHero(ArrayList<Hero> list) {
		String s = "";
		for (Hero h : list)
			s += String.format("%11s ",h.name);
		s+="\n";
		for (Hero h: list) {
			String status = "";
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
			s += String.format("%10s ",status);
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
