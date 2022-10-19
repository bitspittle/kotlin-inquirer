internal val isOldTerminal: Boolean by lazy { System.getProperty("os.name").contains("win", ignoreCase = true) }
