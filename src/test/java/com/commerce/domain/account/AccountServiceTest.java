package com.commerce.domain.account;

import com.commerce.domain.account.dto.CreateAccountDto;
import com.commerce.domain.account.dto.UpdateAccountDto;
import com.commerce.exception.UniqueConstraintViolationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;




@Transactional
@SpringBootTest
class AccountServiceTest {
    @Autowired private AccountRepository accountRepository;
    @Autowired private AccountService accountService;
    @Autowired private EntityManager entityManager;

    private final Faker faker = new Faker();


    @DisplayName("계정을 검색한다.")
    @Test
    void findAccountById() {
        Account initialAccount = Account.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        // given
        initialAccount = accountRepository.save(initialAccount);

        // when
        Account account = accountService.findAccountById(initialAccount.getId());

        // then
        assertThat(account.getId()).isEqualTo(initialAccount.getId());
        assertThat(account.getUsername()).isEqualTo(initialAccount.getUsername());
        assertThat(account.getEmail()).isEqualTo(initialAccount.getEmail());
        assertThat(account.getPhoneNumber()).isEqualTo(initialAccount.getPhoneNumber());
    }

    @DisplayName("존재하지 않는 계정을 검색하면 NoResultException 예외가 발생한다.")
    @Test
    void findAccountByNotExistId() {
        // given
        Long accountId = 10000L;

        // when
        // then
        assertThrows(NoResultException.class, ()-> accountService.findAccountById(accountId));

    }

    @DisplayName("계정을 생성한다.")
    @Test
    void createAccount() {
        // given
        CreateAccountDto createAccountDto = CreateAccountDto
                .builder()
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .username(faker.name().username())
                .password(faker.internet().password())
                .build();

        // when
        Long accountId = accountService.createAccount(createAccountDto);
        Account createdAccount = accountRepository.findById(accountId).get();

        // then
        assertThat(accountId).isNotNull();
        assertThat(createdAccount).isNotNull();
        assertThat(createdAccount.getId()).isEqualTo(createdAccount.getId());
        assertThat(createdAccount.getEmail()).isEqualTo(createdAccount.getEmail());
        assertThat(createdAccount.getPhoneNumber()).isEqualTo(createdAccount.getPhoneNumber());
        assertThat(createdAccount.getPassword()).isEqualTo(createdAccount.getPassword());
    }


    @DisplayName("중복된 이메일로 계정을 생성한다.")
    @Test
    void createAccountByDuplicatedEmail() {
        // given
        Account initialAccount = Account.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();
        accountRepository.save(initialAccount);

        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .email(initialAccount.getEmail())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .username(faker.name().username())
                .password(faker.internet().password())
                .build();

        assertThrows(UniqueConstraintViolationException.class,() -> accountService.createAccount(createAccountDto));
    }

    @DisplayName("중복된 휴대폰번호로 계정을 생성한다.")
    @Test
    void createAccountByDuplicatedPhoneNumber() {
        // given
        Account initialAccount = Account.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();
        accountRepository.save(initialAccount);

        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(initialAccount.getPhoneNumber())
                .password(faker.internet().password())
                .build();

        assertThrows(UniqueConstraintViolationException.class,() -> accountService.createAccount(createAccountDto));
    }

    @DisplayName("중복된 이름으로 계정을 생성한다.")
    @Test
    void createAccountByDuplicatedName() {
        // given
        Account initialAccount = Account.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        initialAccount = accountRepository.save(initialAccount);

        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .username(initialAccount.getUsername())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        assertThrows(UniqueConstraintViolationException.class,() -> accountService.createAccount(createAccountDto));
    }

    @DisplayName("계정을 수정한다.")
    @Test
    void updateAccount() {
        // given
        Account initialAccount = Account.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();
        accountRepository.save(initialAccount);

        UpdateAccountDto updateAccountDto = UpdateAccountDto.builder()
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        // when
        accountService.updateAccount(initialAccount.getId(), updateAccountDto);

        Account updatedAccount = accountRepository.findById(initialAccount.getId()).get();

        // then
        assertThat(updatedAccount).isNotNull();
    }

    @DisplayName("존재하지 않는 계정을 수정하면 에러가 발생한다.")
    @Test
    void updateAccountByNotExistId() {
        // given
        Long accountId = 10000L;

        UpdateAccountDto updateAccountDto = UpdateAccountDto.builder()
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        // when
        // then
        assertThrows(NoResultException.class,() -> accountService.updateAccount(accountId, updateAccountDto));
    }

    @DisplayName("존재하지 않는 계정을 삭제하면 에러가 발생한다.")
    @Test
    void deleteAccountByNotExistId() {
        // given
        Long accountId = 10000L;

        // when
        assertThrows(NoResultException.class,() -> accountService.deleteAccountById(accountId));
    }

    @DisplayName("계정을 삭제한다.")
    @Test
    void deleteAccountById() {
        // given
        Account initialAccount = Account.builder()
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();
        accountRepository.save(initialAccount);

        // when
        accountService.deleteAccountById(initialAccount.getId());
        accountRepository.flush();
        entityManager.clear();

        Optional<Account> deletedAccount = accountRepository.findById(initialAccount.getId());

        // then
        assertThat(deletedAccount.isEmpty()).isTrue();
    }
}