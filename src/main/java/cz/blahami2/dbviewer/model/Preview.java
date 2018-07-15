package cz.blahami2.dbviewer.model;

import lombok.Value;

import java.util.List;

@Value
public class Preview {
    List<String> headers;
    List<List<String>> data;
}
