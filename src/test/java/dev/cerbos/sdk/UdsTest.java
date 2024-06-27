/*
 * Copyright (c) 2021 Zenauth Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.cerbos.sdk;

import dev.cerbos.sdk.builders.Principal;
import dev.cerbos.sdk.builders.Resource;

import java.nio.file.Path;
import java.nio.file.Paths;

import static dev.cerbos.sdk.builders.AttributeValue.stringValue;

public class UdsTest {
  public static void main(String[] args)
      throws CerbosClientBuilder.InvalidClientConfigurationException {
    Path currentRelativePath = Paths.get("");
    String userDir = currentRelativePath.toAbsolutePath().toString();
    System.out.println("Current absolute path is: " + userDir);

    CerbosBlockingClient client =
        new CerbosClientBuilder("unix:/" +userDir + "/cerbos-test/sock/cerbos.grpc")
            .withInsecure()
            .buildBlockingClient();

    CheckResult have =
        client.check(
            Principal.newInstance("john", "employee")
                .withPolicyVersion("20210210")
                .withAttribute("department", stringValue("marketing"))
                .withAttribute("geography", stringValue("GB")),
            Resource.newInstance("leave_request", "xx125")
                .withPolicyVersion("20210210")
                .withAttribute("department", stringValue("marketing"))
                .withAttribute("geography", stringValue("GB"))
                .withAttribute("owner", stringValue("john")),
            "view:public",
            "approve");

    System.out.printf("view:public = %b\n", have.isAllowed("view:public"));
    System.out.printf("approve = %b\n", have.isAllowed("approve"));
  }
}
