package com.commerce.domain.account;

import com.commerce.domain.account.dto.CreateAccountDto;
import com.commerce.domain.account.dto.UpdateAccountDto;
import com.commerce.dummy.DummyAccount;
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
class AccountServiceTest extends DummyAccount {
    @Autowired private AccountRepository accountRepository;
    @Autowired private AccountService accountService;
    @Autowired private EntityManager entityManager;

    private final Faker faker = new Faker();


    @DisplayName("계정을 검색한다.")
    @Test
    void findAccountById() {
        // given
        Account initAccount = accountRepository.save(generateAccount());

        // when
        Account account = accountService.findAccountById(initAccount.getId());

        // then
        assertThat(account.getId()).isEqualTo(initAccount.getId());
        assertThat(account.getName()).isEqualTo(initAccount.getName());
        assertThat(account.getEmail()).isEqualTo(initAccount.getEmail());
        assertThat(account.getPhoneNumber()).isEqualTo(initAccount.getPhoneNumber());
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
                .name(faker.name().fullName())
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
        Account initAccount = generateAccount();
        accountRepository.save(initAccount);

        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .email(initAccount.getEmail())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .name(faker.name().fullName())
                .password(faker.internet().password())
                .build();

        assertThrows(UniqueConstraintViolationException.class,() -> accountService.createAccount(createAccountDto));
    }

    @DisplayName("중복된 휴대폰번호로 계정을 생성한다.")
    @Test
    void createAccountByDuplicatedPhoneNumber() {
        // given
        Account initAccount = generateAccount();
        accountRepository.save(initAccount);

        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .email(faker.internet().emailAddress())
                .phoneNumber(initAccount.getPhoneNumber())
                .name(faker.name().fullName())
                .password(faker.internet().password())
                .build();

        assertThrows(UniqueConstraintViolationException.class,() -> accountService.createAccount(createAccountDto));
    }

    @DisplayName("중복된 이름으로 계정을 생성한다.")
    @Test
    void createAccountByDuplicatedName() {
        // given
        Account initAccount = generateAccount();
        accountRepository.save(initAccount);

        CreateAccountDto createAccountDto = CreateAccountDto.builder()
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .name(initAccount.getName())
                .password(faker.internet().password())
                .build();

        assertThrows(UniqueConstraintViolationException.class,() -> accountService.createAccount(createAccountDto));
    }

    @DisplayName("계정을 수정한다.")
    @Test
    void updateAccount() {
        // given
        Account initAccount = accountRepository.save(generateAccount());

        UpdateAccountDto updateAccountDto = UpdateAccountDto.builder()
                .name(faker.name().fullName())
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .password(faker.internet().password())
                .build();

        // when
        accountService.updateAccount(initAccount.getId(), updateAccountDto);

        Account updatedAccount = accountRepository.findById(initAccount.getId()).get();

        // then
        assertThat(updatedAccount).isNotNull();
    }

    @DisplayName("존재하지 않는 계정을 수정하면 에러가 발생한다.")
    @Test
    void updateAccountByNotExistId() {
        // given
        Long accountId = 10000L;

        UpdateAccountDto updateAccountDto = UpdateAccountDto.builder()
                .name(faker.name().fullName())
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
        Account initAccount = accountRepository.save(generateAccount());

        // when
        accountService.deleteAccountById(initAccount.getId());
        accountRepository.flush();
        entityManager.clear();

        Optional<Account> deletedAccount = accountRepository.findById(initAccount.getId());

        // then
        assertThat(deletedAccount.isEmpty()).isTrue();
    }
}