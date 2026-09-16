class Project {
    public static void main(String[] args) {
        // DND-esc choose your own adventure.
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.println("Welcome to your adventure!");
        System.out.print("Please choose a character class (Warrior, Mage, Ranger): ");
        String characterClass = scanner.nextLine();
        int playerHealth = 100;
        int playerArmorClass = 12;
        int playerAttackBonus = 2;

        if (characterClass.equalsIgnoreCase("Warrior")) {
            System.out.println("You have chosen the Warrior class! Strong and brave.");
            playerArmorClass = 16;
            playerAttackBonus = 3;
        } else if (characterClass.equalsIgnoreCase("Mage")) {
            System.out.println("You have chosen the Mage class! Wise and powerful.");
            playerArmorClass = 10;
            playerAttackBonus = 4;
        } else if (characterClass.equalsIgnoreCase("Ranger")) {
            System.out.println("You have chosen the Ranger class! Agile and stealthy.");
            playerArmorClass = 13;
            playerAttackBonus = 2;
        } else {
            System.out.println("Invalid choice. Please restart the game and choose a valid class.");
        }

        System.out.println("Player Stats: Health = " + playerHealth + ", Armor Class = " + playerArmorClass + ", Attack Bonus = +" + playerAttackBonus);
        System.out.print("\nWhere would you like to go? (Forest, Castle, Village): ");
        String destination = scanner.nextLine();
        if (destination.equalsIgnoreCase("Forest")) {
            System.out.println("\nYou venture into the dark forest, filled with dark creatures. A wild wolf appears!");
        } else if (destination.equalsIgnoreCase("Castle")) {
            System.out.println("\nYou approach the grand castle, where a you are ambushed by a group of bandits!");
        } else if (destination.equalsIgnoreCase("Village")) {
            System.out.println("\nYou head to the village, where a dragon appears and starts attacking the villagers!");
        } else {
            System.out.println("\nInvalid destination. Please restart the game and choose a valid location.");
        }
        java.util.Random random = new java.util.Random();
        int enemyHealth = 100;
        int enemyArmorClass = 12;
        int enemyAttackBonus = 2;
        String enemyName = "enemy";

        if (destination.equalsIgnoreCase("Forest")) {
            enemyName = "Wolf";
            enemyArmorClass = 13;
            enemyAttackBonus = 2;
            enemyHealth = 75;
        } else if (destination.equalsIgnoreCase("Castle")) {
            enemyName = "Bandit";
            enemyArmorClass = 14;
            enemyAttackBonus = 3;
            enemyHealth = 80;
        } else if (destination.equalsIgnoreCase("Village")) {
            enemyName = "Dragon";
            enemyArmorClass = 15;
            enemyAttackBonus = 4;
            enemyHealth = 100;
        }

        String action = "";
        System.out.println("Battle begins! Choose your action each turn. Attack the enemy or heal yourself. \nThe battle continues until either you or the enemy is defeated. \nYou can type 'attack' to attack or 'heal' to heal yourself. Good luck \n(attack/heal):\n");
        while (playerHealth > 0 && enemyHealth > 0) {
            if (!scanner.hasNextLine()) {
                break;
            }

            System.out.flush();
            action = scanner.nextLine();

            if (action.equalsIgnoreCase("attack")) {
                int playerAttackRoll = random.nextInt(20) + 1;
                int totalPlayerAttack = playerAttackRoll + playerAttackBonus;
                if (totalPlayerAttack > enemyArmorClass) {
                    int playerDamage = random.nextInt(15) + 5;
                    enemyHealth -= playerDamage;
                    System.out.println("\nYou roll a " + playerAttackRoll + " + " + playerAttackBonus + " attack bonus = " + totalPlayerAttack + ", hitting the " + enemyName + " for " + playerDamage + " damage. Enemy health: " + Math.max(enemyHealth, 0));
                } else {
                    System.out.println("You roll a " + playerAttackRoll + " + " + playerAttackBonus + " attack bonus = " + totalPlayerAttack + ", which is not high enough to hit the " + enemyName + ".");
                }
            } else if (action.equalsIgnoreCase("heal")) {
                int healAmount = random.nextInt(15) + 10;
                playerHealth = Math.min(playerHealth + healAmount, 100);
                System.out.println("You heal yourself for " + healAmount + " health. Your health: " + playerHealth);
            } else {
                System.out.println("Invalid action. Choose 'attack' or 'heal'.");
                System.out.println();
                continue;
            }

            if (enemyHealth <= 0) {
                break;
            }

            int enemyAttackRoll = random.nextInt(20) + 1;
            int totalEnemyAttack = enemyAttackRoll + enemyAttackBonus;
            if (totalEnemyAttack > playerArmorClass) {
                int enemyDamage = random.nextInt(15) + 5;
                playerHealth -= enemyDamage;
                System.out.println("The " + enemyName + " rolls a " + enemyAttackRoll + " + " + enemyAttackBonus + " attack bonus = " + totalEnemyAttack + ", hitting you for " + enemyDamage + " damage. Your health: " + Math.max(playerHealth, 0) + "\n(attack/heal):");
            } else {
                System.out.println("The " + enemyName + " rolls a " + enemyAttackRoll + " + " + enemyAttackBonus + " attack bonus = " + totalEnemyAttack + ", which misses you. \n(attack/heal):");
            }

            System.out.println();
        }

        if (playerHealth > 0) {
            System.out.println("Congratulations! You have defeated the enemy and completed your adventure! Thank you for playing!");
        } else {
            System.out.println("You have been defeated. Better luck next time!");
        }
        scanner.close();
    }
}