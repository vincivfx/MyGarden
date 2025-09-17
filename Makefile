# ------------------------------------------------------
# Configuration JavaFX et chemins
# ------------------------------------------------------
JAVAFX_HOME = /usr/share/openjfx
JAVAFX_LIB = $(JAVAFX_HOME)/lib

SRC_DIR = src
BIN_DIR = bin
MAIN_CLASS = Main

JAVAC = javac
JAVA  = java

JAVAFX_FLAGS = --module-path $(JAVAFX_LIB) --add-modules javafx.controls

# ------------------------------------------------------
# Cibles
# ------------------------------------------------------

all: clean compile run

# Créer le dossier bin si nécessaire et compiler
compile:
	@mkdir -p $(BIN_DIR)
	$(JAVAC) $(JAVAFX_FLAGS) -d $(BIN_DIR) $(SRC_DIR)/$(MAIN_CLASS).java

# Exécuter l'application
run:
	$(JAVA) $(JAVAFX_FLAGS) -cp $(BIN_DIR) $(MAIN_CLASS)

# Nettoyer les fichiers compilés
clean:
	@rm -rf $(BIN_DIR)
