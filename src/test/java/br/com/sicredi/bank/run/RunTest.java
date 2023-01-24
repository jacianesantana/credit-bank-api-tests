package br.com.sicredi.bank.run;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages("br.com.sicredi.bank.aceitacao")
@IncludeTags("contract")
public class RunTest {
}
