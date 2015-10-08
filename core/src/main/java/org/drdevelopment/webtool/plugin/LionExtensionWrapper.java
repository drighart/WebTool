package org.drdevelopment.webtool.plugin;

import ro.fortsoft.pf4j.ExtensionDescriptor;
import ro.fortsoft.pf4j.ExtensionFactory;
import ro.fortsoft.pf4j.ExtensionWrapper;

public class LionExtensionWrapper<T> implements Comparable<ExtensionWrapper<T>> {

	LionExtensionDescriptor descriptor;
    ExtensionFactory extensionFactory;
    T extension; // cache

	public LionExtensionWrapper(LionExtensionDescriptor descriptor) {
        this.descriptor = descriptor;
	}

	public T getExtension() {
        if (extension == null) {
            extension = (T) extensionFactory.create(descriptor.getExtensionClass());
        }

        return extension;
	}

    public LionExtensionDescriptor getDescriptor() {
        return descriptor;
    }

    public int getOrdinal() {
		return descriptor.getOrdinal();
	}

	@Override
	public int compareTo(ExtensionWrapper<T> o) {
		return (getOrdinal() - o.getOrdinal());
	}

    public void setExtensionFactory(ExtensionFactory extensionFactory) {
        this.extensionFactory = extensionFactory;
    }

}
