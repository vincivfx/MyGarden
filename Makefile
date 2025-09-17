# ------------------------------------------------------
# Configuration JavaFX et chemins
# ------------------------------------------------------

SRC_DIR = src
BIN_DIR = bin
MAIN_CLASS = Main

JAVAC = javac
JAVA  = java

# ------------------------------------------------------
# Cibles
# ------------------------------------------------------

all: clean compile run

# Créer le dossier bin si nécessaire et compiler
compile:
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $(SRC_DIR)/$(MAIN_CLASS).java

# Exécuter l'application
run:
	$(JAVA) -Dprism.order=sw -cp $(BIN_DIR) $(MAIN_CLASS) 

# Nettoyer les fichiers compilés
clean:
	@rm -rf $(BIN_DIR)
