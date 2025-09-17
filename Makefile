# ------------------------------------------------------
# Configuration JavaFX et chemins
# ------------------------------------------------------

SRC_DIR = src
BIN_DIR = bin
MAIN_CLASS = Main

JAVAC = javac
JAVA  = java

all: clean compile run

compile:
	@mkdir -p $(BIN_DIR)
	$(JAVAC) -d $(BIN_DIR) $(SRC_DIR)/$(MAIN_CLASS).java

run:
	$(JAVA) -cp $(BIN_DIR) $(MAIN_CLASS) 


clean:
	@rm -rf $(BIN_DIR)
