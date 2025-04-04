package player;

import player.items.Armor;
import player.items.Weapon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private Weapon weapon;
    private Armor armor;
    private List<String> permanentItems;
    private Map<String, Float> permanentBoosts;

    public Inventory() {
        this.weapon = new Weapon("Bow", 1,1,25);
        // Set base armor here, you can specify default items for helmet, chestplate, leggings, and boots
        this.armor = new Armor("Empty Helmet", "Empty Chestplate", "Empty Leggings", "Empty Boots");
        this.permanentItems = new ArrayList<>();
        this.permanentBoosts = new HashMap<>();
    }

    public Weapon getWeapon() { return weapon; }
    public Armor getArmor() { return armor; }
    public List<String> getPermanentItems() { return permanentItems; }
    public Map<String, Float> getPermanentBoosts() { return permanentBoosts; }

    public void setWeapon(Weapon weapon) { this.weapon = weapon; }
    public void setArmor(Armor armor) { this.armor = armor; }

    public void addPermanentItem(String item) {
        if (!permanentItems.contains(item)) {
            permanentItems.add(item);
        }
    }

    public void addPermanentBoost(String stat, float value) {
        permanentBoosts.put(stat, permanentBoosts.getOrDefault(stat, 0.0f) + value);
    }

    public float getBoost(String stat) {
        return permanentBoosts.getOrDefault(stat, 0.0f);
    }

    public String[] getItems() {
        List<String> items = new ArrayList<>();

        if (weapon != null) items.add("Weapon: " + weapon.getName());
        if (armor != null) items.add("Armor: " + armor.getName());

        items.addAll(permanentItems);

        for (Map.Entry<String, Float> boost : permanentBoosts.entrySet()) {
            items.add("Boost: " + boost.getKey() + " +" + boost.getValue());
        }

        return items.toArray(new String[0]);
    }
}
