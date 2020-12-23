package io.undervolt.utils.config;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class ProfileLoader {

    public transient List<Profile> availableProfiles;
    private final File rootPath;
    public Profile selectedProfile;

    public ProfileLoader(File path) {
        this.rootPath = new File(path + File.separator + "config");
        this.rootPath.mkdirs();
        this.availableProfiles = Lists.newArrayList();
        if(path != null && rootPath.exists() && Objects.requireNonNull(rootPath.listFiles()).length > 0)
            this.getAvailableProfiles(rootPath);
        else {
            try {
                this.newProfile("Unnamed profile");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getAvailableProfiles(final File folder) {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                this.availableProfiles.add(new Profile(fileEntry.getName(), fileEntry));
            }
            else return;
        }
        this.selectedProfile = availableProfiles.get(0);
    }

    public Profile newProfile(String name) {
        final File file = new File(rootPath + File.separator + name);
        file.mkdirs();

        Profile newProf = new Profile(name, file);
        this.setNewProfile(newProf);
        this.availableProfiles.add(newProf);
        return newProf;
    }

    public void setNewProfile(Profile profile) {
        this.selectedProfile = profile;
    }

    public static final class Profile {
        private final String name;
        private final File file;

        public String getName() {
            return name;
        }

        public File getFile() {
            return file;
        }

        public Profile(String name, File file) {
            this.file = file;
            this.name = name;
        }
    }

    public void setSelectedProfile(Profile selectedProfile) {
        this.selectedProfile = selectedProfile;
    }
}
