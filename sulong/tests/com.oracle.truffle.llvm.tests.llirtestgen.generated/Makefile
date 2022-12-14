#
# Copyright (c) 2019, 2022, Oracle and/or its affiliates.
#
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without modification, are
# permitted provided that the following conditions are met:
#
# 1. Redistributions of source code must retain the above copyright notice, this list of
# conditions and the following disclaimer.
#
# 2. Redistributions in binary form must reproduce the above copyright notice, this list of
# conditions and the following disclaimer in the documentation and/or other materials provided
# with the distribution.
#
# 3. Neither the name of the copyright holder nor the names of its contributors may be used to
# endorse or promote products derived from this software without specific prior written
# permission.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
# OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
# COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
# EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
# GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
# AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
# NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
# OF THE POSSIBILITY OF SUCH DAMAGE.
#

.PHONY: default

ifndef LLIRTESTGEN_CMD
  $(error LLIRTESTGEN_CMD not set)
endif
OUTPUT_DIR=gen
TIMESTAMP=timestamp
SCRIPTS_DIR=scripts
SCRIPTS=$(SCRIPTS_DIR)/gep-vector.sh $(SCRIPTS_DIR)/gep-vector-header.sh $(SCRIPTS_DIR)/gep-vector-footer.sh $(SCRIPTS_DIR)/gep-vector-test.sh

default: $(TIMESTAMP)

$(TIMESTAMP): $(LLIR_TEST_GEN_JAR) $(SCRIPTS_DIR) $(SCRIPTS)
	@mkdir -p $(OUTPUT_DIR)
	$(QUIETLY) touch $@
	$(QUIETLY) $(LLIRTESTGEN_CMD) $(OUTPUT_DIR)
	cd $(SCRIPTS_DIR); ./gep-vector.sh > ../$(OUTPUT_DIR)/gep-vector.ll


$(SCRIPTS_DIR):
	@mkdir -p $(SCRIPTS_DIR)

$(SCRIPTS_DIR)/gep-vector.sh: gep-vector.sh
$(SCRIPTS_DIR)/gep-vector-header.sh: gep-vector-header.sh
$(SCRIPTS_DIR)/gep-vector-footer.sh: gep-vector-footer.sh
$(SCRIPTS_DIR)/gep-vector-test.sh: gep-vector-test.sh

$(SCRIPTS_DIR)/%:
	@mkdir -p scripts
	cp -f $< $@
