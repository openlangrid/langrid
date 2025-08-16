package org.langrid.wrapper.chatgpt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.go.nict.langrid.repackaged.net.arnx.jsonic.JSON;
import jp.go.nict.langrid.service_1_2.ProcessFailedException;

public class ChatGPTService {
	public ChatGPTService() {
		json = new JSON();
		json.setSuppressNull(true);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String execute(String prompt) throws ProcessFailedException{
		try {
			var con = (HttpURLConnection)  new URL(url).openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", "Bearer " + apikey);
			con.setRequestProperty("Content-Type", "application/json");
			var input = new Input("user");
			input.getContent().add(Content.inputText(prompt));
			var req = new Request(model);
			req.getInput().add(input);
			con.setDoOutput(true);
			try(var w = new OutputStreamWriter(con.getOutputStream())){
				w.write(json.format(req));
				w.flush();
			}

			try(var br = new BufferedReader(new InputStreamReader(con.getInputStream()))){
				var response = JSON.decode(br, Response.class);
				for(var o : response.getOutput()) {
					if(o.getType().equals(Output.TYPE_MESSAGE)) {
						for(var c : o.getContent()) {
							if(c.getType().equals(Content.TYPE_OUTPUT_TEXT)) {
								return (c.getText());
							}
						}
					}
				}
				throw new ProcessFailedException("invalid response. " + JSON.encode(response, true));
			}
		} catch (IOException e) {
			throw new ProcessFailedException(e);
		}
	}

	private String url = "https://api.openai.com/v1/responses";
	private String apikey;
	private String model = "gpt-5-nano";
	private JSON json;
}


class Request{
	public Request() {
	}
	public Request(String model) {
		this.model = model;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public List<Input> getInput() {
		return input;
	}
	public void setInput(List<Input> input) {
		this.input = input;
	}
	private String model = "gpt-5-nano"; // "gpt-5", "gpt-5-mini"
	private List<Input> input = new ArrayList<>();
}

class Input{
	public Input() {
	}
	public Input(String role) {
		this.role = role;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public List<Content> getContent() {
		return content;
	}
	public void setContent(List<Content> content) {
		this.content = content;
	}
	private String role = "user";
	private List<Content> content = new ArrayList<>();
}

class Content{
	public static final String TYPE_INPUT_TEXT = "input_text";
	public static final String TYPE_INPUT_IMAGE = "input_image";
	public static final String TYPE_OUTPUT_TEXT = "output_text";

	public static Content inputText(String text) {
		var ret = new Content(TYPE_INPUT_TEXT);
		ret.setText(text);
		return ret;
	}
	
	public Content() {
	}

	public Content(String type) {
		this.type = type;
	}
	

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getImageUrl() {
		return image_url;
	}
	public void setImageUrl(String image_url) {
		this.image_url = image_url;
	}
	public List<Object> getAnnotations() {
		return annotations;
	}
	public void setAnnotations(List<Object> annotations) {
		this.annotations = annotations;
	}
	public List<Object> getLogprobs() {
		return logprobs;
	}
	public void setLogprobs(List<Object> logprobs) {
		this.logprobs = logprobs;
	}
	private String type; // "input_text", "input_image", "output_text"
	// type=="input_text" or "output_text
	private String text; // "What is in this image?";
	// type=="input_image
	private String image_url; // "https://openai-documentation.vercel.app/images/cat_and_otter.png";
	// type=="output_text
	private List<Object> annotations; // [],
	private List<Object> logprobs; // [],
}


class Response{
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isBackground() {
		return background;
	}
	public void setBackground(boolean background) {
		this.background = background;
	}
	public Error getError() {
		return error;
	}
	public void setError(Error error) {
		this.error = error;
	}
	public Object getIncomplete_details() {
		return incomplete_details;
	}
	public void setIncomplete_details(Object incomplete_details) {
		this.incomplete_details = incomplete_details;
	}
	public Object getInstructions() {
		return instructions;
	}
	public void setInstructions(Object instructions) {
		this.instructions = instructions;
	}
	public Object getMax_output_tokens() {
		return max_output_tokens;
	}
	public void setMax_output_tokens(Object max_output_tokens) {
		this.max_output_tokens = max_output_tokens;
	}
	public Object getMax_tool_calls() {
		return max_tool_calls;
	}
	public void setMax_tool_calls(Object max_tool_calls) {
		this.max_tool_calls = max_tool_calls;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public List<Output> getOutput() {
		return output;
	}
	public void setOutput(List<Output> output) {
		this.output = output;
	}
	public boolean isParallel_tool_calls() {
		return parallel_tool_calls;
	}
	public void setParallel_tool_calls(boolean parallel_tool_calls) {
		this.parallel_tool_calls = parallel_tool_calls;
	}
	public Object getPrevious_response_id() {
		return previous_response_id;
	}
	public void setPrevious_response_id(Object previous_response_id) {
		this.previous_response_id = previous_response_id;
	}
	public Object getPrompt_cache_key() {
		return prompt_cache_key;
	}
	public void setPrompt_cache_key(Object prompt_cache_key) {
		this.prompt_cache_key = prompt_cache_key;
	}
	private String id; // "resp_689ffe8e44388193bf7f9de0344ed9550aab0dd5ae163eab",
	private String object; // "response",
	private long created_at; // 1755315854,
	private String status; // "completed",
	private boolean background; // false,
	private Error error; // null,
	private Object incomplete_details; // null,
	private Object instructions; // null,
	private Object max_output_tokens; // null,
	private Object max_tool_calls; // null,
	private String model; // "gpt-5-nano-2025-08-07",
	private List<Output> output = new ArrayList<>();
	private boolean parallel_tool_calls; // true,
	private Object previous_response_id; // null,
	private Object prompt_cache_key; // null,
/*	  "reasoning": {
	    "effort": "medium",
	    "summary": null
	  },
	  "safety_identifier": null,
	  "service_tier": "default",
	  "store": true,
	  "temperature": 1.0,
	  "text": {
	    "format": {
	      "type": "text"
	    },
	    "verbosity": "medium"
	  },
	  "tool_choice": "auto",
	  "tools": [],
	  "top_logprobs": 0,
	  "top_p": 1.0,
	  "truncation": "disabled",
	  "usage": {
	    "input_tokens": 15,
	    "input_tokens_details": {
	      "cached_tokens": 0
	    },
	    "output_tokens": 3711,
	    "output_tokens_details": {
	      "reasoning_tokens": 3136
	    },
	    "total_tokens": 3726
	  },
	  "user": null,
	  "metadata": {}
*/
}

class Output{
	public static final String TYPE_REASONING = "reasoning";
	public static final String TYPE_MESSAGE = "message";

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public List<Object> getSummary() {
		return summary;
	}
	public void setSummary(List<Object> summary) {
		this.summary = summary;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Content> getContent() {
		return content;
	}
	public void setContent(List<Content> content) {
		this.content = content;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	private String id; // "rs_689ffe8f22a8819398fa6f618c7d82480aab0dd5ae163eab",
	private String type; // "reasoning" or "message"
	// type=="reasoning"
	private List<Object> summary = new ArrayList<>(); // []
	// type=="message"
	private String status; // "completed",
	private List<Content> content;
	private String role; // "role": "assistant"
}

class Error{
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getParam() {
		return param;
	}
	public void setParam(Object param) {
		this.param = param;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	private String message; // "You exceeded your current quota, please check your plan and billing details. For more information on this error, read the docs: https://platform.openai.com/docs/guides/error-codes/api-errors.",
	private String type; // "insufficient_quota",
	private Object param; // null,
	private String code; // "insufficient_quota"
}
