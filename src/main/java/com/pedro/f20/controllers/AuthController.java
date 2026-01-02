
@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping
    public ResponseEntity<MessageDTO> register(@RequestBody @Valid UserRegisterDTO data) {
        UserRegisterDTO processedData = 
            data.isAdmin() ? data : new UserRegisterDTO(
                data.user(),
                data.name(),
                data.ra(),
                data.groupId(),
                data.unitId(),
                data.permissionMenu(),
                data.tag(),
                data.isAdmin()
            );

        UserDataComplete userCreated = service.create(processedData, (User) user);
        MessageDTO messageDto = createMessageUtil.createMessage(201, userCreated);
        return ResponseEntity.ok().body(messageDto);
    }
}