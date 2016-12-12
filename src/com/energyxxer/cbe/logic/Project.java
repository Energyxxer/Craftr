package com.energyxxer.cbe.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.energyxxer.cbe.global.Preferences;
import com.energyxxer.cbe.main.FileSelector;
import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.minecraft.MinecraftConstants;
import com.energyxxer.cbe.ui.dialogs.ProjectProperties;
import com.energyxxer.cbe.util.StringUtil;

public class Project {

	public File directory;
	public File source;
	private String name = null;

	private String prefix = null;
	private String world = null;
	
	public HashMap<String, String> icons = new HashMap<String, String>();
	
	public Project(String name) {
		this.directory = new File(Preferences.get("workspace_dir") + File.separator + name);
		this.source = new File(Preferences.get("workspace_dir") + File.separator + name + File.separator + "src");
		this.name = name;
		this.prefix = StringUtil.getInitials(name).toLowerCase();
		this.icons.put("src","src");
	}
	
	public Project(File directory) {
		this.directory = directory;
		this.source = new File(directory.getAbsolutePath() + File.separator + "src");
		File config = new File(directory.getAbsolutePath() + File.separator + ".project");
		if(config != null && config.exists() && config.isFile() && config.getName().equals(".project")) {
			byte[] encoded;
			try {
				encoded = Files.readAllBytes(config.toPath());
			} catch (IOException e) {
				this.name = this.prefix = this.world = null;
				this.directory = null;
				e.printStackTrace();
				return;
			}
			
			this.directory = config.getParentFile();
			
			String s = new String(encoded);
			
			List<String> lines = Arrays.asList(s.split("\n"));
			for(String line : lines) {
				if(line.contains("=")) {
					String key = line.substring(0,line.indexOf("="));
					String value = line.substring(line.indexOf("=")+1);

					if(value.length() <= 0) continue;
					
					if(key.equals("name")) {
						this.name = value;
					} else if(key.equals("prefix")) {
						this.prefix = value;
					} else if(key.equals("out")) {
						this.world = value;
					} else if(key.equals("icons")) {
						String[] files = value.split("\\|");
						for(String file : files) {
							String[] segments = file.split("\\?");
							icons.put(segments[0].intern(), segments[1]);
						}
					}
				}
			}
			return;
		}
		this.directory = null;
		this.source = null;
		throw new RuntimeException("Invalid configuration file.");
	}
	
	public Project rename(String name) throws IOException {
		File newFile = new File(Preferences.get("workspace_dir") + File.separator + name);
		if(newFile.exists()) {
			throw new IOException("A project by that name already exists!");
		}
		this.name = name;
		updateConfig();
		return this;
	}
	
	public Project updateConfig() {
		File config = new File(directory.getAbsolutePath() + File.separator + ".project");
		PrintWriter writer;
		try {
			writer = new PrintWriter(config, "UTF-8");
			
			writer.print(getRawConfig());
			
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public boolean exists() {
		return directory != null && directory.exists();
	}
	
	public Project createNew() {
		if(!exists()) {
			this.directory.mkdirs();
			this.source.mkdirs();
			File config = new File(directory.getAbsolutePath() + File.separator + ".project");
			try {
				config.createNewFile();
				updateConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return this;
	}
	
	private Project createFromName(String name) {
		this.name = name;
		this.prefix = StringUtil.getInitials(name).toLowerCase();
		return this;
	}
	
	public Project fixIfCorrupted() {
		if((this.name == null || this.prefix == null) && this.directory != null) {
			createFromName(directory.getName());
			updateConfig();
		}
		return this;
	}
	
	public void promptOutput() {
		String path = FileSelector.create("Select world", "<html>Specify the world directory to output to.<br>Structures will be saved in the structures folder.<br>The resource pack (if present) will be saved to this location, too.</html>", MinecraftConstants.getMinecraftDir() + File.separator + "saves" + File.separator, FileSelector.OPEN_DIRECTORY);
		if(path != null) {
			world = path;
			updateConfig();
		}
	}
	
	public String getRawConfig() {
		String s = "";
		s += "name=" + name + "\n";
		s += "prefix=" + prefix + "\n";
		if(world != null) 
			s += "out=" + world + "\n";
		{
			String entry = "";
			Iterator<String> it = icons.keySet().iterator();
			while(it.hasNext()) {
				String key = it.next();
				String value = icons.get(key);
				String file = key + "?" + value;
				entry += file;
				if(it.hasNext()) entry += "|";
			}
			s += "icons=" + entry;
		}
		return s;
	}
	
	public String getRelativePath(File file) {
		if(!file.getAbsolutePath().startsWith((directory.getAbsolutePath()+File.separator))) return null;
		return file.getAbsolutePath().substring((directory.getAbsolutePath()+File.separator).length());
	}
	
	public Project setIconFor(File file, String value) {
		String path = getRelativePath(file);
		if(path != null) {
			icons.put(path.intern(), value);
			updateConfig();
		}
		return this;
	}
	
	public String getIconFor(File file) {
		String path = getRelativePath(file);
		if(path != null) {
			String icon = icons.get(path);
			if(icon == null) return null;
			return (icon.startsWith("*") ? icon.substring(1) : Window.theme.path + icon);
		}
		return null;
	}
	
	public enum ProjectFrom {
		NAME,
		CONFIG
	};
	
	public String getName() {
		return name;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getWorld() {
		return world;
	}
	
	@Override
	public String toString() {
		return String.format("Project [name=%s, prefix=%s, world=%s]", name, prefix, world);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public void showPropertiesDialog() {
		ProjectProperties.show(this);
	}
}
