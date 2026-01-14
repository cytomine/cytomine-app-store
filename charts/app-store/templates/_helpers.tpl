{{- define "cytomine.podSecurityContext" }}
runAsUser: {{ .Values.containerSecurity.userID }}
runAsGroup: {{ .Values.containerSecurity.groupID }}
fsGroup: {{ .Values.containerSecurity.fsgroupID }}
supplementalGroups: [4000]
seccompProfile:
    type: "RuntimeDefault"
{{- end }}

{{- define "cytomine.containerSecurityContext" }}
allowPrivilegeEscalation: {{ .Values.containerSecurity.allowPrivilegeEscalation }}
privileged: {{ .Values.containerSecurity.privileged }}
readOnlyRootFilesystem: {{ .Values.containerSecurity.readOnlyRootFilesystem }}
runAsNonRoot: true
capabilities:
    drop: ["ALL"]
{{- end }}
