package idas.chox.core.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public enum TaskType {
	CLIENT_ALLEGATIONS  ("Client Allegations"),
	ENG_INSPECTION      ("Engineer Inspection"),
	INVOICE_ISSUE       ("Invoice/Payment Issue"),
	PAYMENT_PACK        ("Payment Pack"),
	REPAIR_DOCS         ("Repair Documentation"),
	TELEPHONE_CALL      ("Telephone Call"),
	TOTAL_LOSS_PACK     ("Total Loss Pack"),
        TOTAL_LOSS_PAYMENT  ("Total Loss Payment"),
	WITNESS_STATEMENT   ("Witness Statement(s)"),
	UNDEFINED_TASK      ("Other") {
            @Override
            public boolean isInsurerPrivateType(){
		return true;
            }
            @Override
            public boolean isCHOPrivateType(){
		return true;
            }
        };

	private static final Logger LOG = LoggerFactory.getLogger(TaskType.class);
	private static Map<String, String> insurerTasks;
	private static Map<String, String> choTasks;
	private static Map<String, String> privateTasks;
        private final String description;

        TaskType(String description){ this.description = description;}

        public String getDescription() {
            return description;
        }
	public boolean isInsurerInternalType(){
            return true;
	}

	public boolean isCHOInternalType(){
            return true;
	}

	public boolean isInsurerExternalType(){
            return true;
	}

	public boolean isCHOExternalType(){
            return true;
	}

        public boolean isInsurerPrivateType(){
            return true;
	}
        public boolean isCHOPrivateType(){
            return true;
	}


	public static Map<String, String> getPrivateTaskTypes() {
		if ( privateTasks == null) {
			privateTasks = new LinkedHashMap();
			for (int i = 0; i < values().length; i++) {
				TaskType array_element = values()[i];
				if (array_element.isInsurerPrivateType() ||  array_element.isCHOPrivateType()) {
					LOG.debug("Private Task Type: {} ", array_element.toString());
					privateTasks.put(array_element.toString(), array_element.getDescription());
				}
			}

		}
		return privateTasks;

        }
	public static Map<String, String> getInsurerInternalTaskTypes() {
		if ( insurerTasks == null) {
			insurerTasks = new LinkedHashMap();
			for (int i = 0; i < values().length; i++) {
				TaskType array_element = values()[i];
				if (array_element.isInsurerInternalType()) {
					LOG.debug("Insurer Internal Task Type: {} ", array_element.toString());
					insurerTasks.put(array_element.toString(), array_element.getDescription());
				}
			}

		}
		return insurerTasks;
	}

	public static Map<String, String> getInsurerExternalTaskTypes() {
		if ( insurerTasks == null) {
			insurerTasks = new LinkedHashMap();
			for (int i = 0; i < values().length; i++) {
				TaskType array_element = values()[i];
				if (array_element.isInsurerExternalType()) {
					LOG.debug("Insurer External Task Type: {} ", array_element.toString());
					insurerTasks.put(array_element.toString(), array_element.getDescription());
				}
			}

		}
		return insurerTasks;
	}

	public static Map<String, String> getChoInternalTaskTypes() {
		if ( choTasks == null) {
			choTasks = new LinkedHashMap();
			for (int i = 0; i < values().length; i++) {
				TaskType array_element = values()[i];
				if (array_element.isCHOInternalType()) {
					LOG.debug("Cho Internal Task Type: {}", array_element.toString());
					choTasks.put(array_element.toString(), array_element.getDescription());
				}
			}

		}
		return choTasks;
	}

	public static Map<String, String> getChoExternalTaskTypes() {
		if ( choTasks == null) {
			choTasks = new LinkedHashMap();
			for (int i = 0; i < values().length; i++) {
				TaskType array_element = values()[i];
				if (array_element.isCHOExternalType()) {
					LOG.debug("Cho External Task Type: {}", array_element.toString());
					choTasks.put(array_element.toString(), array_element.getDescription());
				}
			}

		}
		return choTasks;
	}
}
