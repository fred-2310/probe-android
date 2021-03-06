package org.openobservatory.ooniprobe.model.settings;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.openobservatory.engine.Engine;
import org.openobservatory.engine.OONIMKTaskConfig;
import org.openobservatory.ooniprobe.BuildConfig;
import org.openobservatory.ooniprobe.common.PreferenceManager;
import org.openobservatory.ooniprobe.common.ReachabilityManager;

import java.util.Arrays;
import java.util.List;

public class Settings {
	@SerializedName("annotations")
	public final Annotations annotations;

	@SerializedName("assets_dir")
	private String assets_dir;

	@SerializedName("disabled_events")
	public final List<String> disabled_events;

	@SerializedName("log_level")
	public final String log_level;

	@SerializedName("options")
	public final Options options;

	@SerializedName("inputs")
	public List<String> inputs;

	@SerializedName("name")
	public String name;

	@SerializedName("state_dir")
	private String state_dir;

	@SerializedName("temp_dir")
	private String temp_dir;

	public Settings(Context c, PreferenceManager pm) {
		annotations = new Annotations(c, pm);
		disabled_events = Arrays.asList("status.queued", "status.update.websites", "failure.report_close");
		log_level = pm.isDebugLogs() ? "DEBUG2" : "INFO";
		options = new Options(c, pm);
	}

	public OONIMKTaskConfig toExperimentSettings(Gson gson, Context c) throws java.io.IOException {
		assets_dir = Engine.getAssetsDir(c);
		state_dir = Engine.getStateDir(c);
		temp_dir = Engine.getTempDir(c);
		return new OONIMKTaskConfigAdapter(gson, this);
	}

	private class OONIMKTaskConfigAdapter implements OONIMKTaskConfig {
		private String serialized;
		private Settings settings;

		OONIMKTaskConfigAdapter(Gson gson, Settings settings) {
			this.serialized = gson.toJson(settings);
			this.settings = settings;
		}

		public String taskName() {
			return settings.name;
		}

		public String serialization() {
			return this.serialized;
		}
	}

	public static class Annotations {
		@SerializedName("network_type")
		public final String network_type;
		@SerializedName("flavor")
		public final String flavor;
		@SerializedName("origin")
		public String origin;

		public Annotations(Context c, PreferenceManager pm) {
			if (pm.isIncludeAsn())
				this.network_type = ReachabilityManager.getNetworkType(c);
			else
				this.network_type = null;
			this.flavor = BuildConfig.FLAVOR;
		}
	}

	public static class Options {
		@SerializedName("no_collector")
		public boolean no_collector;
		@SerializedName("save_real_probe_asn")
		public final boolean save_real_probe_asn;
		@SerializedName("save_real_probe_cc")
		public final boolean save_real_probe_cc;
		@SerializedName("save_real_probe_ip")
		public final boolean save_real_probe_ip;
		@SerializedName("software_name")
		public final String software_name;
		@SerializedName("software_version")
		public final String software_version;
		@SerializedName("randomize_input")
		public final boolean randomize_input;
		@SerializedName("no_file_report")
		public final boolean no_file_report;
		@SerializedName("max_runtime")
		public Integer max_runtime;
		@SerializedName("server")
		public String server;
		@SerializedName("probe_services_base_url")
		public String probe_services_base_url;

		public Options(Context c, PreferenceManager pm) {
			no_collector = !pm.isUploadResults();
			save_real_probe_asn = pm.isIncludeAsn();
			save_real_probe_cc = pm.isIncludeCc();
			save_real_probe_ip = pm.isIncludeIp();
			software_name = BuildConfig.SOFTWARE_NAME;
			software_version = BuildConfig.VERSION_NAME;
			randomize_input = false;
			no_file_report = true;
		}
	}
}
