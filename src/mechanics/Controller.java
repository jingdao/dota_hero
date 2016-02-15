package mechanics;
import java.util.ArrayList;

public class Controller {

	public Controller() {
		ArrayList<Hero> my_team = new ArrayList<Hero>();
		my_team.add(Hero.Earthshaker);
		my_team.add(Hero.Lina);
		my_team.add(Hero.AntiMage);
		my_team.add(Hero.Doom);
		my_team.add(Hero.Tusk);
		ArrayList<Hero> enemy_team = new ArrayList<Hero>();
		enemy_team.add(Hero.Clinkz);
		enemy_team.add(Hero.Viper);
		enemy_team.add(Hero.Dazzle);
		enemy_team.add(Hero.Leshrac);
		enemy_team.add(Hero.Enigma);
		displayHero(enemy_team);
		System.out.println("\n\n");
		displayHero(my_team);
	}

	public void displayHero(ArrayList<Hero> list) {
		String s = "";
		for (Hero h : list)
			s += String.format("%11s ",h.name);
		s+="\n";
		for (Hero h : list)
			s += String.format("%5d/%5d ",(int)h.current_hp,(int)h.hp);
		s+="\n";
		for (Hero h : list)
			s += String.format("%5d/%5d ",(int)h.current_mana,(int)h.mana);
		s+="\n";
		System.out.println(s);
	}

	public static void main(String[] args) {
		Controller c = new Controller();
	}
}
