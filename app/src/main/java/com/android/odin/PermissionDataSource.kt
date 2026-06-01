package com.android.odin

enum class Severity {
    NONE,
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL,
    ASTRONOMICAL
}

data class PermissionRisk(
    val permission: String,
    val severity: Severity,
    val weight: Double,
    val description: String
)

val permissionRiskMap = mapOf(

    // ASTRONOMICAL
    "android.permission.INSTALL_PACKAGES" to PermissionRisk(
        "android.permission.INSTALL_PACKAGES",
        Severity.ASTRONOMICAL,
        10.0,
        "Permite instalar pacotes no dispositivo"
    ),

    // CRITICAL
    "android.permission.COPY_PROTECTED_DATA" to PermissionRisk(
        "android.permission.COPY_PROTECTED_DATA",
        Severity.CRITICAL,
        10.0,
        "Permite copiar dados protegidos do sistema"
    ),

    "android.permission.WRITE_SECURE_SETTINGS" to PermissionRisk(
        "android.permission.WRITE_SECURE_SETTINGS",
        Severity.CRITICAL,
        10.0,
        "Permite modificar configurações seguras do Android"
    ),

    "android.permission.READ_FRAME_BUFFER" to PermissionRisk(
        "android.permission.READ_FRAME_BUFFER",
        Severity.CRITICAL,
        10.0,
        "Permite capturar o conteúdo exibido na tela"
    ),

    "android.permission.MANAGE_CA_CERTIFICATES" to PermissionRisk(
        "android.permission.MANAGE_CA_CERTIFICATES",
        Severity.CRITICAL,
        10.0,
        "Permite gerenciar certificados de autoridade confiáveis"
    ),

    "android.permission.MANAGE_APP_OPS_MODES" to PermissionRisk(
        "android.permission.MANAGE_APP_OPS_MODES",
        Severity.CRITICAL,
        10.0,
        "Permite alterar operações internas de permissões"
    ),

    "android.permission.GRANT_RUNTIME_PERMISSIONS" to PermissionRisk(
        "android.permission.GRANT_RUNTIME_PERMISSIONS",
        Severity.CRITICAL,
        10.0,
        "Permite conceder permissões em tempo de execução"
    ),

    "android.permission.DUMP" to PermissionRisk(
        "android.permission.DUMP",
        Severity.CRITICAL,
        10.0,
        "Permite acessar informações internas de depuração do sistema"
    ),

    android.Manifest.permission.CAMERA to PermissionRisk(
        android.Manifest.permission.CAMERA,
        Severity.CRITICAL,
        10.0,
        "Permite acessar a câmera do dispositivo"
    ),

    "android.permission.SYSTEM_CAMERA" to PermissionRisk(
        "android.permission.SYSTEM_CAMERA",
        Severity.CRITICAL,
        10.0,
        "Permite controle privilegiado da câmera do sistema"
    ),

    "android.permission.MANAGE_PROFILE_AND_DEVICE_OWNERS" to PermissionRisk(
        "android.permission.MANAGE_PROFILE_AND_DEVICE_OWNERS",
        Severity.CRITICAL,
        10.0,
        "Permite gerenciar proprietários de perfil e dispositivo"
    ),

    "android.permission.MOUNT_UNMOUNT_FILESYSTEMS" to PermissionRisk(
        "android.permission.MOUNT_UNMOUNT_FILESYSTEMS",
        Severity.CRITICAL,
        10.0,
        "Permite montar e desmontar sistemas de arquivos"
    ),

    // HIGH
    "android.permission.INSTALL_GRANT_RUNTIME_PERMISSIONS" to PermissionRisk(
        "android.permission.INSTALL_GRANT_RUNTIME_PERMISSIONS",
        Severity.HIGH,
        7.5,
        "Permite instalar e conceder permissões em tempo de execução"
    ),

    android.Manifest.permission.READ_SMS to PermissionRisk(
        android.Manifest.permission.READ_SMS,
        Severity.HIGH,
        7.5,
        "Permite ler mensagens SMS"
    ),

    android.Manifest.permission.SEND_SMS to PermissionRisk(
        android.Manifest.permission.SEND_SMS,
        Severity.HIGH,
        7.5,
        "Permite enviar mensagens SMS"
    ),

    "android.permission.WRITE_SMS" to PermissionRisk(
        "android.permission.WRITE_SMS",
        Severity.HIGH,
        7.5,
        "Permite modificar mensagens SMS"
    ),

    "android.permission.RECEIVE_MMS" to PermissionRisk(
        "android.permission.RECEIVE_MMS",
        Severity.HIGH,
        7.5,
        "Permite receber mensagens MMS"
    ),

    "android.permission.SEND_SMS_NO_CONFIRMATION" to PermissionRisk(
        "android.permission.SEND_SMS_NO_CONFIRMATION",
        Severity.HIGH,
        7.5,
        "Permite enviar SMS sem confirmação do usuário"
    ),

    "android.permission.RECEIVE_SMS" to PermissionRisk(
        "android.permission.RECEIVE_SMS",
        Severity.HIGH,
        7.5,
        "Permite receber mensagens SMS"
    ),

    "android.permission.READ_LOGS" to PermissionRisk(
        "android.permission.READ_LOGS",
        Severity.HIGH,
        7.5,
        "Permite ler logs internos do sistema"
    ),

    "android.permission.READ_PRIVILEGED_PHONE_STATE" to PermissionRisk(
        "android.permission.READ_PRIVILEGED_PHONE_STATE",
        Severity.HIGH,
        7.5,
        "Permite acessar informações privilegiadas do estado do telefone"
    ),

    "android.permission.LOCATION_HARDWARE" to PermissionRisk(
        "android.permission.LOCATION_HARDWARE",
        Severity.HIGH,
        7.5,
        "Permite acessar recursos de hardware de localização"
    ),

    android.Manifest.permission.ACCESS_FINE_LOCATION to PermissionRisk(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        Severity.HIGH,
        7.5,
        "Permite acessar localização precisa"
    ),

    "android.permission.ACCESS_BACKGROUND_LOCATION" to PermissionRisk(
        "android.permission.ACCESS_BACKGROUND_LOCATION",
        Severity.HIGH,
        7.5,
        "Permite acessar localização em segundo plano"
    ),

    "android.permission.BIND_ACCESSIBILITY_SERVICE" to PermissionRisk(
        "android.permission.BIND_ACCESSIBILITY_SERVICE",
        Severity.HIGH,
        7.5,
        "Permite vincular serviço de acessibilidade"
    ),

    android.Manifest.permission.ACCESS_WIFI_STATE to PermissionRisk(
        android.Manifest.permission.ACCESS_WIFI_STATE,
        Severity.HIGH,
        7.5,
        "Permite acessar informações sobre redes Wi-Fi"
    ),

    "com.android.voicemail.permission.READ_VOICEMAIL" to PermissionRisk(
        "com.android.voicemail.permission.READ_VOICEMAIL",
        Severity.HIGH,
        7.5,
        "Permite ler mensagens de correio de voz"
    ),

    android.Manifest.permission.RECORD_AUDIO to PermissionRisk(
        android.Manifest.permission.RECORD_AUDIO,
        Severity.HIGH,
        7.5,
        "Permite gravar áudio"
    ),

    "android.permission.CAPTURE_AUDIO_OUTPUT" to PermissionRisk(
        "android.permission.CAPTURE_AUDIO_OUTPUT",
        Severity.HIGH,
        7.5,
        "Permite capturar saída de áudio do dispositivo"
    ),

    "android.permission.ACCESS_NOTIFICATIONS" to PermissionRisk(
        "android.permission.ACCESS_NOTIFICATIONS",
        Severity.HIGH,
        7.5,
        "Permite acessar notificações"
    ),

    "android.permission.INTERACT_ACROSS_USERS_FULL" to PermissionRisk(
        "android.permission.INTERACT_ACROSS_USERS_FULL",
        Severity.HIGH,
        7.5,
        "Permite interação completa entre usuários do dispositivo"
    ),

    "android.permission.BLUETOOTH_PRIVILEGED" to PermissionRisk(
        "android.permission.BLUETOOTH_PRIVILEGED",
        Severity.HIGH,
        7.5,
        "Permite acesso privilegiado a recursos Bluetooth"
    ),

    "android.permission.GET_PASSWORD" to PermissionRisk(
        "android.permission.GET_PASSWORD",
        Severity.HIGH,
        7.5,
        "Permite acessar senhas armazenadas"
    ),

    "android.permission.INTERNAL_SYSTEM_WINDOW" to PermissionRisk(
        "android.permission.INTERNAL_SYSTEM_WINDOW",
        Severity.HIGH,
        7.5,
        "Permite criar janelas internas do sistema"
    ),

    // MEDIUM
    android.Manifest.permission.ACCESS_COARSE_LOCATION to PermissionRisk(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        Severity.MEDIUM,
        5.0,
        "Permite acessar localização aproximada"
    ),

    "android.permission.CHANGE_COMPONENT_ENABLED_STATE" to PermissionRisk(
        "android.permission.CHANGE_COMPONENT_ENABLED_STATE",
        Severity.MEDIUM,
        5.0,
        "Permite alterar o estado de componentes da aplicação"
    ),

    android.Manifest.permission.READ_CONTACTS to PermissionRisk(
        android.Manifest.permission.READ_CONTACTS,
        Severity.MEDIUM,
        5.0,
        "Permite ler contatos do usuário"
    ),

    android.Manifest.permission.WRITE_CONTACTS to PermissionRisk(
        android.Manifest.permission.WRITE_CONTACTS,
        Severity.MEDIUM,
        5.0,
        "Permite modificar contatos do usuário"
    ),

    "android.permission.CONNECTIVITY_INTERNAL" to PermissionRisk(
        "android.permission.CONNECTIVITY_INTERNAL",
        Severity.MEDIUM,
        5.0,
        "Permite acessar funcionalidades internas de conectividade"
    ),

    "android.permission.ACCESS_MEDIA_LOCATION" to PermissionRisk(
        "android.permission.ACCESS_MEDIA_LOCATION",
        Severity.MEDIUM,
        5.0,
        "Permite acessar localização associada a arquivos de mídia"
    ),

    android.Manifest.permission.READ_EXTERNAL_STORAGE to PermissionRisk(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        Severity.MEDIUM,
        5.0,
        "Permite ler arquivos do armazenamento externo"
    ),

    android.Manifest.permission.WRITE_EXTERNAL_STORAGE to PermissionRisk(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Severity.MEDIUM,
        5.0,
        "Permite modificar arquivos do armazenamento externo"
    ),

    "android.permission.SYSTEM_ALERT_WINDOW" to PermissionRisk(
        "android.permission.SYSTEM_ALERT_WINDOW",
        Severity.MEDIUM,
        5.0,
        "Permite exibir janelas sobre outros aplicativos"
    ),

    "android.permission.READ_CALL_LOG" to PermissionRisk(
        "android.permission.READ_CALL_LOG",
        Severity.MEDIUM,
        5.0,
        "Permite ler histórico de chamadas"
    ),

    "android.permission.WRITE_CALL_LOG" to PermissionRisk(
        "android.permission.WRITE_CALL_LOG",
        Severity.MEDIUM,
        5.0,
        "Permite modificar histórico de chamadas"
    ),

    "android.permission.INTERACT_ACROSS_USERS" to PermissionRisk(
        "android.permission.INTERACT_ACROSS_USERS",
        Severity.MEDIUM,
        5.0,
        "Permite interação entre usuários do dispositivo"
    ),

    "android.permission.MANAGE_USERS" to PermissionRisk(
        "android.permission.MANAGE_USERS",
        Severity.MEDIUM,
        5.0,
        "Permite gerenciar usuários do dispositivo"
    ),

    android.Manifest.permission.READ_CALENDAR to PermissionRisk(
        android.Manifest.permission.READ_CALENDAR,
        Severity.MEDIUM,
        5.0,
        "Permite ler eventos do calendário"
    ),

    android.Manifest.permission.WRITE_CALENDAR to PermissionRisk(
        android.Manifest.permission.WRITE_CALENDAR,
        Severity.MEDIUM,
        5.0,
        "Permite modificar eventos do calendário"
    ),

    "android.permission.BLUETOOTH_ADMIN" to PermissionRisk(
        "android.permission.BLUETOOTH_ADMIN",
        Severity.MEDIUM,
        5.0,
        "Permite administrar conexões Bluetooth"
    ),

    android.Manifest.permission.BODY_SENSORS to PermissionRisk(
        android.Manifest.permission.BODY_SENSORS,
        Severity.MEDIUM,
        5.0,
        "Permite acessar sensores corporais"
    ),

    "android.permission.MANAGE_EXTERNAL_STORAGE" to PermissionRisk(
        "android.permission.MANAGE_EXTERNAL_STORAGE",
        Severity.MEDIUM,
        5.0,
        "Permite acesso amplo ao armazenamento externo"
    ),

    // LOW
    android.Manifest.permission.INTERNET to PermissionRisk(
        android.Manifest.permission.INTERNET,
        Severity.LOW,
        2.5,
        "Permite acesso à internet"
    ),

    "android.permission.POST_NOTIFICATIONS" to PermissionRisk(
        "android.permission.POST_NOTIFICATIONS",
        Severity.LOW,
        2.5,
        "Permite publicar notificações"
    ),

    "android.permission.DOWNLOAD_WITHOUT_NOTIFICATION" to PermissionRisk(
        "android.permission.DOWNLOAD_WITHOUT_NOTIFICATION",
        Severity.LOW,
        2.5,
        "Permite realizar downloads sem notificação visível"
    ),

    "android.permission.PACKAGE_USAGE_STATS" to PermissionRisk(
        "android.permission.PACKAGE_USAGE_STATS",
        Severity.LOW,
        2.5,
        "Permite acessar estatísticas de uso dos aplicativos"
    ),

    "android.permission.MASTER_CLEAR" to PermissionRisk(
        "android.permission.MASTER_CLEAR",
        Severity.LOW,
        2.5,
        "Permite executar restauração completa do dispositivo"
    ),

    "android.permission.DELETE_PACKAGES" to PermissionRisk(
        "android.permission.DELETE_PACKAGES",
        Severity.LOW,
        2.5,
        "Permite remover pacotes instalados"
    ),

    "android.permission.GET_PACKAGE_SIZE" to PermissionRisk(
        "android.permission.GET_PACKAGE_SIZE",
        Severity.LOW,
        2.5,
        "Permite consultar o tamanho de pacotes instalados"
    ),

    android.Manifest.permission.BLUETOOTH to PermissionRisk(
        android.Manifest.permission.BLUETOOTH,
        Severity.LOW,
        2.5,
        "Permite comunicação via Bluetooth"
    ),

    "android.permission.DEVICE_POWER" to PermissionRisk(
        "android.permission.DEVICE_POWER",
        Severity.LOW,
        2.5,
        "Permite acesso a recursos de energia do dispositivo"
    ),

    // NONE
    android.Manifest.permission.ACCESS_NETWORK_STATE to PermissionRisk(
        android.Manifest.permission.ACCESS_NETWORK_STATE,
        Severity.NONE,
        0.0,
        "Permissão sem impacto relevante para o modelo de risco"
    ),

    android.Manifest.permission.RECEIVE_BOOT_COMPLETED to PermissionRisk(
        android.Manifest.permission.RECEIVE_BOOT_COMPLETED,
        Severity.NONE,
        0.0,
        "Permissão sem impacto relevante para o modelo de risco"
    ),

    android.Manifest.permission.WAKE_LOCK to PermissionRisk(
        android.Manifest.permission.WAKE_LOCK,
        Severity.NONE,
        0.0,
        "Permissão sem impacto relevante para o modelo de risco"
    ),

    android.Manifest.permission.VIBRATE to PermissionRisk(
        android.Manifest.permission.VIBRATE,
        Severity.NONE,
        0.0,
        "Permissão sem impacto relevante para o modelo de risco"
    )
)